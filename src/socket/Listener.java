package socket;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import context.ContractContext;
import context.FunctionCallContext;
import context.FunctionDefinitionContext;
import context.VariableContext;
import node.*;
import rulecheck.RuleChecker;
import util.Position;
import util.Printer;
import util.Scanner;

public class Listener extends Thread {
	int nowVersion = 1;
	int minVersion = 1;
	Socket socket;
	String clientIP = "";
	boolean isSuccess = false;
	int number = 1;
	static boolean isRun = false;

	public Listener(Socket socket, int number) {
		this.socket = socket;
		clientIP = socket.getRemoteSocketAddress().toString();
		this.number = number;
	}

	@Override
	public void run() {
		int cnt = 0;
		while (isRun) {
			cnt++;
			if ((cnt % 1000) == 0) {
				System.out.println("waiting for next turn.. [" + number + "]");
			}
		}
		
		try {
			isRun = true;
			
			byte[] confirm = { 1 };

			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();

			// Receive code capacity
			byte[] receiveBuffer = new byte[1024];
			input.read(receiveBuffer);
			String strSize = new String(receiveBuffer);
			strSize = strSize.trim();

			int size = 0;
			size = Integer.parseInt(strSize);
			System.out.println("Listener solidiyCount : " + size);

			output.write(confirm);

			// receive code
			byte[] bytesSolidity = new byte[size];

			// remaining iterations
			int max = 65000;
			int loopSize = bytesSolidity.length;
			System.out.println("loopSize : " + loopSize);
			int loopCount = 0;
			int remain = 0;

			loopCount = loopSize / max;
			remain = loopSize % max;

			int i = 0;
			for (i = 0; i < loopCount; i++) {
				input.read(bytesSolidity, (i * max), max);
			}
			input.read(bytesSolidity, (i * max), remain);

			String solidity = new String(bytesSolidity);
			solidity = solidity.trim();
			// System.out.println("Listener solidity : " + solidity);
			System.out.println("loopCount : " + loopCount);
			System.out.println("remain : " + remain);

//    		analyze(solidity);

			// send Resulting Capacity
			String result = analyze(solidity);
			String count = Integer.toString(result.length());
			byte[] bytesCount = count.getBytes();
			System.out.println("Listener resultCount : " + count);
			output.write(bytesCount);

			input.read(confirm);

			// send result
			output.write(result.getBytes());

			// socket.close();
			isRun = false;
		} catch (Exception e) {
			e.printStackTrace();
			isRun = false;
		}
	}

	public String analyze(String solidity) {
		// create .sol
		String sol = "solidity/web/solidity_ex_" + number + ".sol";
		try {
			OutputStream outputStream = new FileOutputStream(sol);
			String str = solidity;
			byte[] by = str.getBytes();
			outputStream.write(by);
		} catch (Exception e) {
			e.getStackTrace();
			return "{\"error\":\"creating solidity file\"}";
		}

		// create AST
		String exeTime = "********** EXECUTION TIME **********\r\n";
		long _start;
		long start;
		long end;

		String inputFile = sol;
		String json = "";

		Runtime rt = Runtime.getRuntime();
		Process pc = null;
		try {
//			String command = "solc_batch.bat " + inputFile;		// Windows
			String command = "./solc.sh " + inputFile;		// Linux
			// String command = "telegramBot";
			pc = rt.exec(command);
			pc.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"creating AST file\"}";
		} finally {
			pc.destroy();
			System.out.println("solc exec!");
		}

		// analyze
		try {
			_start = System.currentTimeMillis();
			Scanner scanner = new Scanner();
			json = scanner.createJson(inputFile);
			end = System.currentTimeMillis();
			exeTime += String.format("scanning time : %.3f sec\r\n", (end - _start) / 1000.0);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"creating JSON\"}";
		}

		try {
			start = System.currentTimeMillis();
			ParsingToAST parsingToAST = new ParsingToAST();
			JSONObject jsonObject = parsingToAST.parse(json);
			parsingToAST.visitNode(jsonObject);
			end = System.currentTimeMillis();
			exeTime += String.format("parsing time : %.3f sec\r\n", (end - start) / 1000.0);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"parsing JSON to AST\"}";
		}

		Position.setup(inputFile);

		try {
			start = System.currentTimeMillis();
			Printer printer = new Printer();
			printer.init();
			printer.emit(inputFile);
			end = System.currentTimeMillis();
			exeTime += String.format("emitting time : %.3f sec\r\n", (end - start) / 1000.0);
		} catch (Exception e) {
			e.printStackTrace();
//			return "error : info print";
		}

		String result = null;
		try {
			start = System.currentTimeMillis();
			RuleChecker ruleChecker = new RuleChecker();
			ruleChecker.ruleCheck();
			end = System.currentTimeMillis();
			exeTime += String.format("ruleChecking time : %.3f sec\r\n", (end - start) / 1000.0);

			exeTime += String.format("total time : %.3f sec\r\n", (end - _start) / 1000.0);

			System.out.println(ruleChecker.getResults());
			System.out.println(ruleChecker.getCriticityCount());

			JSONObject jsonObj = new JSONObject(); //
			result = jsonObj.toJSONString(ruleChecker.getResultMap()); //
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"rule check\"}";
		}

		try {
			ContractContext contractContext = new ContractContext();
			List<ContractDefinition> contractContextList = contractContext.getAllContract();
			VariableContext variableContext = new VariableContext();
			List<VariableDeclaration> variableList = variableContext.getAllVariables();
			FunctionDefinitionContext functionDefinitionContext = new FunctionDefinitionContext();
			List<FunctionDefinition> functionDefinitionList = functionDefinitionContext.getAllFunctionDefinitions();
			FunctionCallContext functionCallContext = new FunctionCallContext();
			List<FunctionCall> functionCallsList = functionCallContext.getAllFunctionCalls();
			System.out.println("********** CONTEXT COUNT **********");
			System.out.println("contract : " + contractContextList.size());
			System.out.println("variable : " + variableList.size());
			System.out.println("funcDef : " + functionDefinitionList.size());
			System.out.println("funcCall : " + functionCallsList.size());
			System.out.println();
			System.out.println(exeTime);

			ContractDefinition.clearRegistry();
			DoWhileStatement.clearRegistry();
			EventDefinition.clearRegistry();
			Expression.clearRegistry();
			ExpressionStatement.clearRegistry();
			ForStatement.clearRegistry();
			FunctionCall.clearRegistry();
			FunctionDefinition.clearRegistry();
			Identifier.clearRegistry();

			IfStatement.clearRegistry();
			ModifierDefinition.clearRegistry();
			VariableDeclaration.clearRegistry();
			VariableDeclarationStatement.clearRegistry();
			WhileStatement.clearRegistry();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
