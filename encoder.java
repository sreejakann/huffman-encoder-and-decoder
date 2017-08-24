
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

class heapnode{

	heapnode(int d, int f){
		frequency = f;
		data = d;
		left = null;
		right = null;
	}
	int data;
	int frequency;
	heapnode left;
	heapnode right;

	@Override
	public String toString() {
		return "(d:"+data+",f:"+frequency+")";
	}
}

class pairnode{
	int data;
	int frequency;
	pairnode left;
	pairnode right;
	pairnode child;
	pairnode ls;
	pairnode rs;
	pairnode(int d, int f){
		frequency = f;
		data = d;
		ls = rs = null;
		left = right = child = null;
	}
}

class binheap {
	int heap_size = 0;
	public void minHeapify(heapnode[] arr, int i){
		int left = 2*i;
		int right = 2*i+1;
		int min = 0;
		if(left < heap_size && arr[left].frequency < arr[i].frequency){
			min = left;
		}
		else
			min = i;
		if(right < heap_size && arr[right].frequency < arr[min].frequency)
			min = right;
		if(min != i){
			swap(arr, i, min);
			minHeapify(arr, min);

		}
	}

	public heapnode[] build_min_heap(HashMap<Integer, Integer> map){
		heapnode[] heap_table = new heapnode[map.size()+1];
		Set<Integer> set = map.keySet();
		Iterator<Integer> it = set.iterator();
		heapnode hn;
		while(it.hasNext()){
			int key = it.next();
			int val = map.get(key);
			hn = new heapnode(key,val);
			insert(heap_table,hn);
		}
		return heap_table;

	}
	public heapnode[] swap(heapnode[] A, int l, int r){
		heapnode temp = A[l];
		A[l] = A[r];
		A[r] = temp;
		return A;
	}


	public heapnode removeMin(heapnode[] A){
		if(heap_size < 1){
			return null;
		}
		heapnode minnode = A[1];
		A[1] = A[heap_size];
		heap_size--;
		minHeapify(A,1);
		return minnode;
	}

	public void insert(heapnode[] A, heapnode h){
		heap_size++;
		A[heap_size] = h;
		int i = heap_size;
		int p = i/2;
		while(i>1 && A[p].frequency > A[i].frequency){
			swap(A,i,p);
			i = p;
			p = i/2;
		}
	}

	public heapnode build_tree_using_binary_heap(HashMap<Integer, Integer> freq_table){

		heap_size = 0;
		heapnode[] heap_table = build_min_heap(freq_table);
		heapnode fst;
		heapnode sc;
		heapnode n;
		while(heap_size > 1){
			fst = removeMin(heap_table);
			sc = removeMin(heap_table);
			n = new heapnode(-1, fst.frequency + sc.frequency);
			n.left = fst;
			n.right = sc;
			insert(heap_table,n);
		}
		return heap_table[1];
	}
}

class pairingHeap{
	pairnode root;

	public pairnode build_tree_using_pairing_heap(HashMap<Integer, Integer> freq_table){

		build_pair_heap(freq_table);
		pairnode fst;
		pairnode sc;
		pairnode n;
		while(root.child != null){
			fst = removeMin();
			sc = removeMin();
			n = new pairnode(-1, fst.frequency + sc.frequency);
			n.left = fst;
			n.right = sc;
			insert(n);
		}
		return root;
	}

	public void build_pair_heap(HashMap<Integer, Integer> freq_table){
		//pairnode[] pair_heap_table = new pairnode[freq_table.size()+1];
		Set<Integer> set = freq_table.keySet();
		Iterator<Integer> it = set.iterator();
		pairnode pn;
		while(it.hasNext()){
			int key = it.next();
			int val = freq_table.get(key);
			pn = new pairnode(key,val);
			insert(pn);
		}
	}

	public void insert(pairnode n){
		if (root == null)
			root = n;
		else
			root = meldTwoSubtrees(root, n);

	}

	public pairnode combineSiblilngs(pairnode fsib){
		if(fsib.rs == null)
			return fsib;
		LinkedList<pairnode> siblist = new LinkedList<pairnode>();
		pairnode node = fsib;
		pairnode noderight = fsib.rs;
		while(node!=null){
			siblist.add(node);
			noderight = node;
			node = noderight.rs;
			noderight.rs = null;

		}

		node = siblist.removeFirst();
		noderight = siblist.removeFirst();
		LinkedList<pairnode> list = new LinkedList<pairnode>();
		while(node !=null){
			node = meldTwoSubtrees(node, node.right);
			list.addLast(node);
			if(siblist.isEmpty() == false){
				node = siblist.removeFirst();
				if(siblist.isEmpty() == true){
					noderight = list.removeLast();
					pairnode n = meldTwoSubtrees(noderight, node);
					list.addLast(n);
					break;
				}
				else{
					noderight = list.removeFirst();
				}
			}
			else
				node = null;

		}
		pairnode rnode = list.removeLast();
		while(!(list.isEmpty())){
			node = list.removeLast();
			rnode = meldTwoSubtrees(rnode,node);
		}

		return rnode;
	}

	public pairnode removeMin(){
		   if (root == null)
	            return null;
	        pairnode min_node = root;
	        if (root.child == null)
	            root = null;
	        else
	            root = combineSiblilngs( root.child );
	        return min_node;

	}
	public pairnode meldTwoSubtrees(pairnode firstNode, pairnode secondNode){
		if(secondNode == null)
			return firstNode;
		if(firstNode.frequency > secondNode.frequency){
			secondNode.ls = firstNode.ls;
			firstNode.ls = secondNode;
			firstNode.rs = secondNode.child;
			if(firstNode.rs != null){
				firstNode.rs.ls = firstNode;
			}
			secondNode.child = firstNode;
			return secondNode;
		}
		else{
			secondNode.ls = firstNode;
			firstNode.rs = secondNode.rs;
			if(firstNode.rs != null){
				firstNode.rs.ls = firstNode;
			}
			secondNode.rs = firstNode.child;
			if(secondNode.rs != null){
				secondNode.rs.ls = secondNode;
			}
			firstNode.child = secondNode;
			return firstNode;
		}
	}

}

class fourwayheap{
	int heap_size;
	private void minHeapify(heapnode[] arr, int i){
		if(i<3)
			return;

		int child = 0;
		int min = arr[i].frequency;
		int index = i;

		for(int k = 0;k<4;k++){
			child = 4*(i-2) + k;
			if(child <= heap_size && arr[child].frequency < min){
				min = arr[child].frequency;
				index = child;
			}
		}

		if(index != i){
			swap(arr, i, index);
			minHeapify(arr, index);

		}
	}

	public heapnode[] build_min_heap(HashMap<Integer, Integer> map){
		heapnode[] heap_table = new heapnode[map.size()+3];
		Set<Integer> set = map.keySet();
		Iterator<Integer> it = set.iterator();
		heapnode hn;
		int cntr =3;
		heap_table[0] = null;
		heap_table[1] = null;
		heap_table[2] = null;

		while(it.hasNext()){
			int key = it.next();
			int val = map.get(key);
			hn = new heapnode(key,val);
			heap_table[cntr] = hn;
			cntr++;
		}
		heap_size = heap_table.length-1;
		build_heap(heap_table);
		return heap_table;

	}

	public void build_heap(heapnode[] ht){
			int lc = ht.length;
			int p = lc/4 +2;
			for(int i = p; i>2; i--){
				minHeapify(ht, i);
			}
	}
	public void swap(heapnode[] A, int l, int r){
		heapnode temp = A[l];
		A[l] = A[r];
		A[r] = temp;
	}


	public heapnode removeMin(heapnode[] A){
		if(heap_size < 3){
			return null;
		}
		heapnode minnode = A[3];
		if (heap_size == 3) {
			heap_size--;
			A[3] = null;
			return minnode;
		}

		A[3] = A[heap_size];
		A[heap_size] = null;
		heap_size--;
		minHeapify(A,3);
		return minnode;
	}

	public void insert(heapnode[] A, heapnode h){
		heap_size++;
		A[heap_size] = h;
		int i = heap_size;
		int p = i/4 + 2;
		while(p>3 && A[p].frequency > A[i].frequency){
			swap(A,i,p);
			i = p;
			p = i/4 + 2;
		}
	}

	public heapnode build_tree_using_four_way_heap(HashMap<Integer, Integer> freq_table){

		heap_size = 2;
		heapnode[] heap_table = build_min_heap(freq_table);
		heapnode fst;
		heapnode sc;
		heapnode n;
		while(heap_size > 3){
			fst = removeMin(heap_table);
			sc = removeMin(heap_table);
			n = new heapnode(-1, fst.frequency + sc.frequency);
			n.left = fst;
			n.right = sc;
			insert(heap_table,n);
		}
		return heap_table[3];
	}
}

public class encoder {

	public void buildTree(HashMap<Integer, Integer> freq_table){
		binheap bh = new binheap();
		fourwayheap fh = new fourwayheap();
		pairingHeap ph = new pairingHeap();
		for(int i=0;i<10;i++){
			bh.build_tree_using_binary_heap(freq_table);
		}
		for(int i=0;i<10;i++){
			fh.build_tree_using_four_way_heap(freq_table);
		}
		for(int i=0;i<10;i++){
			ph.build_tree_using_pairing_heap(freq_table);
		}
	}

	public HashMap<Integer, String> build_ct(HashMap<Integer, Integer> freq_table){
		binheap bh = new binheap();
		fourwayheap fh = new fourwayheap();
		heapnode h = fh.build_tree_using_four_way_heap(freq_table);

		HashMap<Integer, String> ct = new HashMap<Integer, String>();
		String s = "";

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		build_code_table(h, ct, s);
		return ct;
	}

	public void build_code_table(heapnode node, HashMap<Integer, String> ct, String s ){

			if (node.left == null && node.right == null) {
				ct.put(node.data, s);
			}
			if(node.left != null) {
				build_code_table(node.left, ct, s+0);
			}
			if (node.right != null) {
				build_code_table(node.right, ct, s+1);
			}
	}

	public void write_to_code_table(HashMap<Integer, String> hmap){
		StringBuilder sbo = new StringBuilder("");
		String fname = "code_table.txt";
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(fname));
			for (int key: hmap.keySet()) {
				sbo.append(key);
				sbo.append(" ");
				sbo.append(hmap.get(key));
				try {
					br.write(sbo.toString());
					br.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				sbo.setLength(0);
			}
			br.close();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void write_to_encoded_bin(HashMap<Integer, String> hmap,ArrayList<Integer> arr){
		int len = arr.size();
		String str;
		StringBuilder sb = new StringBuilder("");
		byte b;
		String buffer = "";

		String fileName = "encoded.bin";
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(fileName),false);
			OutputStream out = new BufferedOutputStream(fos);
			for(int l=0;l<len;l++){

				str = hmap.get(arr.get(l));
				sb.append(str);
				int i =0;
				int strlen = sb.length();
				while(i<(strlen /8)){
					b = (byte) Integer.parseInt(sb.substring(8 * i, 8 * (i + 1)), 2);
					out.write(b);
					i++;
				}
				int rem = strlen % 8;
				buffer = sb.substring(strlen-rem, strlen);
				sb.setLength(0);
				sb.append(buffer);
			}
			out.close();
			fos.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]){

		//String file_name = "C:\\Users\\sreej\\workspace\\ADS\\src\\com\\project\\sample_input_large.txt";
		String file_name = args[0];
		Scanner sc;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		HashMap<Integer, Integer> freq_table = new HashMap<Integer, Integer>();

		try {
			sc = new Scanner(new FileReader(file_name));
			while(sc.hasNext()){
				int data = sc.nextInt();
				arr.add(data);
				if(freq_table.containsKey(data)){
					int val = freq_table.get(data);
					freq_table.put(data, val+1);
				}
				else{
					freq_table.put(data, 1);
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		encoder h1 = new encoder();
		//h1.buildTree(freq_table);

		HashMap<Integer, String> hmap = h1.build_ct(freq_table);
		h1.write_to_code_table(hmap);
		h1.write_to_encoded_bin(hmap, arr);


	}
}
