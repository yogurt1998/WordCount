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
			System.out.println("�����������ʽ��[parameter] [file_name]��");
			
			while (true) {
				Scanner scanner = new Scanner(System.in); // ��ȡ��������
				
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
					System.out.println("�������������룡");
				}
			}
			
				String filepath = "D:\\test\\" + filename;
				AFile aFile = new AFile();
				aFile = findFiles(filepath);
				if (bString.equals("-c"))
					System.out.println("�ַ���Ϊ" + aFile.charNumb);
				else if (bString.equals("-w"))
					System.out.println("������Ϊ" + aFile.wordNumb);
				else if (bString.equals("-l"))
					System.out.println("����Ϊ" + aFile.lineNumb);
				else if (bString.equals("-s"))
					System.out.println("�ַ���Ϊ" + aFile.charNumb +
							"\n������Ϊ" + aFile.wordNumb +
							"\n����Ϊ" + aFile.lineNumb + 
							"\n������Ϊ��" + aFile.empleLine +
							"\n��������Ϊ��" + aFile.codeLine +
							"\nע������Ϊ��" + aFile.nodeLine);
				else if (bString.equals("-a"))
					System.out.println("������Ϊ��" + aFile.empleLine 
							+ " ��������Ϊ��" + aFile.codeLine + " ע������Ϊ��" + aFile.nodeLine);
			
		} while(true);
		
	}
	
	private static AFile findFiles(String file_path) {
		File file = new File(file_path);
		AFile aFile = new AFile();
		
		if (file.isDirectory()) { // �ж�Ϊ�ļ���
			File[] files = file.listFiles();
			if (files == null) {
				System.err.println("�Ҳ���");
			} else if (files.length == 0) {
				System.out.println("Ŀ¼Ϊ��");
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
