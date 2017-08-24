
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

class dnode{
	dnode(int key){
		this.key = key;
		left = right = null;
	}
	int key;
	dnode left;
	dnode right;
}

public class decoder {

	public dnode build_decode_table(String cf_name){
		BufferedReader br = null;
		dnode root = new dnode(-1);
		String str;
		try {
			br = new BufferedReader(new FileReader(cf_name));
			while((str = br.readLine()) != null){
				String[] sarr = str.split(" ");
				int key = Integer.parseInt(sarr[0]);
				String s = sarr[1];

				dnode temp = root;

				int len = s.length();
				int i=0;
				while(i<len){
					if(s.charAt(i) == '0'){
						if(temp.left == null){
							if(i == len-1)
								temp.left = new dnode(key);
							else
								temp.left = new dnode(-1);
						}
						temp = temp.left;
					}
					else{
						if(temp.right == null){
							if(i == len-1)
								temp.right = new dnode(key);
							else
								temp.right = new dnode(-1);
						}
						temp = temp.right;
					}
					i++;
				}

			}br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}


		// Iterates through the file to get each line and splits each line to get key and its value
		// builds decode_tabe using values obtained from code_table.txt


		return root;
	}

	public ArrayList<Integer> get_decoded_array(dnode root, String ef_name){


		InputStream is = null;
		ArrayList<Integer> oarr = new ArrayList<Integer>();
		try {
			is = new FileInputStream(ef_name);
			int k;
			String str;
			dnode tmp = root;
			while((k=is.read())!=-1){
				str = Integer.toBinaryString(k);
				int len = str.length();
				if(len < 8){
					for(int i=0; i<(8-len);i++){
						str = '0'+str;
					}
				}
				int i=0;
				while(i<8){
					if(str.charAt(i) == '0')
						tmp = tmp.left;
					else
						tmp = tmp.right;
					if(tmp.left==null && tmp.right==null){
						oarr.add(tmp.key);
						tmp = root;
					}
					i++;

				}

			}
			is.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}


		return oarr;


	}

	public static void main(String args[]){
		decoder d = new decoder();
		String ef_name = args[0];
		String cf_name = args[1];
		dnode root = d.build_decode_table(cf_name);
		ArrayList<Integer> arr = d.get_decoded_array(root,ef_name);
		String fname = "decoded.txt";

		try(BufferedWriter br = new BufferedWriter(new FileWriter(fname,false))) {

			int ln = arr.size();
			for(int j=0;j<ln;j++){
				br.write(String.valueOf(arr.get(j)) + '\n');

			}
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


	}
}
