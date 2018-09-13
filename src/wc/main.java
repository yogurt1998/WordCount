package wc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PrimitiveIterator;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		do {
			String filename = null;
			String bString = null;
			System.out.println("请输入命令（格式：[parameter] [file_name]）");
			
			while (true) {
				Scanner scanner = new Scanner(System.in); // 获取键盘输入
				
				if (scanner.hasNext()) {
					bString = scanner.next();
				}
				
				if (bString.equals("-c") || bString.equals("-w")
						|| bString.equals("-l") || bString.equals("-s")
						|| bString.equals("-a")) {
					if (scanner.hasNextLine()) {
						filename = scanner.next();
					}
					break;
				} else {
					System.out.println("错误！请重新输入！");
				}
			}
			
				String filepath = "D:\\test\\" + filename;
				AFile aFile = new AFile();
				aFile = findFiles(filepath);
				if (bString.equals("-c"))
					System.out.println("字符数为" + aFile.charNumb);
				else if (bString.equals("-w"))
					System.out.println("单词数为" + aFile.wordNumb);
				else if (bString.equals("-l"))
					System.out.println("行数为" + aFile.lineNumb);
				else if (bString.equals("-s"))
					System.out.println("字符数为" + aFile.charNumb +
							"\n单词数为" + aFile.wordNumb +
							"\n行数为" + aFile.lineNumb + 
							"\n空行数为：" + aFile.empleLine +
							"\n代码行数为：" + aFile.codeLine +
							"\n注释行数为：" + aFile.nodeLine);
				else if (bString.equals("-a"))
					System.out.println("空行数为：" + aFile.empleLine 
							+ " 代码行数为：" + aFile.codeLine + " 注释行数为：" + aFile.nodeLine);
			
		} while(true);
		
	}
	
	private static AFile findFiles(String file_path) {
		File file = new File(file_path);
		AFile aFile = new AFile();
		
		if (file.isDirectory()) { // 判断为文件夹
			File[] files = file.listFiles();
			if (files == null) {
				System.err.println("找不到");
			} else if (files.length == 0) {
				System.out.println("目录为空");
			} else {
				for (File f : files) {
					if (f.isDirectory())
						findFiles(f.getPath());
					else if (f.isFile()) {
						aFile.charNumb += getCount(f.getPath()).charNumb;
						aFile.wordNumb += getCount(f.getPath()).wordNumb;
						aFile.lineNumb += getCount(f.getPath()).lineNumb;
						aFile.codeLine += getCount(f.getPath()).codeLine;
						aFile.empleLine += getCount(f.getPath()).empleLine;
						aFile.nodeLine += getCount(f.getPath()).nodeLine;
					}
				}
			}
		} else if (file.isFile()) { 
			aFile = getCount(file.getPath());
		}
		
		return aFile;
	}
	
	private static AFile getCount(String filepath) {
		AFile aFile = new AFile();
		try {
			BufferedReader brin = new BufferedReader(new FileReader(filepath));
			String s;
			int state = 0;
			
			String regxNodeBegin = "(\\S?)\\s*/\\*.*";
			String regxNodeEnd = "(.*\\*/\\s*)\\S?";
			String regxNode = "(\\s*)(\\S?)(//+).*";
			
			while ((s = brin.readLine()) != null) {
				++ aFile.lineNumb;
				int countLetter = 0;
				
				for (int i = 0; i < s.length(); i++) {
					++aFile.charNumb;
					Character c = s.charAt(i);
					++countLetter;
					if (c == ' ' || c == '\n' || c == '\t') {
						state = 0;
						--countLetter;
					}
					else if (state == 0) {
						state = 1;
						++aFile.wordNumb;
					}
				}
				
				if (s.matches(regxNodeBegin) || s.matches(regxNodeEnd)
						|| s.matches(regxNode))
					++aFile.nodeLine;
				else if (countLetter > 1)
					++aFile.codeLine;
				else 
					++aFile.empleLine;
			}
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		
		return aFile;
	}
	
}
