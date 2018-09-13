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
				
				File file = new File(filepath);
				if (!file.exists()) {
					System.out.println("文件不存在");
					continue;
				}
				
				AFile aFile = new AFile();
				aFile = findFiles(filepath);
				
				if (bString.equals("-c"))
					System.out.println("字符数为" + aFile.charNumb);
				else if (bString.equals("-w"))
					System.out.println("单词数为" + aFile.wordNumb);
				else if (bString.equals("-l"))
					System.out.println("行数为" + aFile.lineNumb);
				else if (bString.equals("-a"))
					System.out.println("空行数为：" + aFile.empleLine 
							+ " 代码行数为：" + aFile.codeLine + " 注释行数为：" + aFile.nodeLine);
			
		} while(true);
		
	}
	
	private static AFile findFiles(String file_path) {

		File file = new File(file_path);
		AFile aFile = new AFile();
		
		if (file.isDirectory()) { // 判断为文件夹
			File[] files = file.listFiles(); // 获取文件列表
			if (files == null) {
				System.err.println("找不到");
			} else if (files.length == 0) {
				System.out.println("目录为空");
			} else {
				for (File f : files) { // 循环处理文件
					if (f.isDirectory()) // 判断为文件夹
						findFiles(f.getPath());
					else if (f.isFile()) { // 判断为文件
						System.out.println("文件名为：" + f.getName() + "\n字符数为" + getCount(f.getPath()).charNumb +
								"\n单词数为" + getCount(f.getPath()).wordNumb +
								"\n行数为" + getCount(f.getPath()).lineNumb + 
								"\n空行数为：" + getCount(f.getPath()).empleLine +
								"\n代码行数为：" + getCount(f.getPath()).codeLine +
								"\n注释行数为：" + getCount(f.getPath()).nodeLine);
					}
				}
			}
		} else if (file.isFile() && file.exists()) { // 判断为文件
			aFile = getCount(file.getPath());
		}
		
		return aFile;
	}
	
	private static AFile getCount(String filepath) {
		AFile aFile = new AFile();
		try {
			BufferedReader brin = new BufferedReader(new FileReader(filepath));
			String s;
			int state = 0; // 判断是否在单词内
			
			// 使用正则表达式判断注释行
			String regxNodeBegin = "(\\S?)\\s*/\\*.*";
			String regxNodeEnd = "(.*\\*/\\s*)\\S?";
			String regxNode = "(\\s*)(\\S?)(//+).*";
			
			while ((s = brin.readLine()) != null) {
				++ aFile.lineNumb;
				int countLetter = 0; // 判断非空格字符数量
				
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
				else if (countLetter > 1) // 如果非空格字符数多于1
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
