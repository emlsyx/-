
/*
* author: acme
*   date: 2017-1-19
*  blogs: http://blog.csdn.net/qq_18297675
* update: 2017-1-25
*   info: change node class become inner private class
*/

public class CycleLinkList<T> {

	private class Node{  //实现该接口后，可进行对象的复制
		
		public T data;
		public Node next;
		Node(T d) {
			data = d;
		}
	}
	private Node head;    //头节点
	private Node tail;    //尾指针
	private int iCount = 0;      		//链表大小
	
	public CycleLinkList() {
		initList();
	}
	
	//初始化链表
	public void initList() {
		head = new Node(null);
		tail = new Node(null);
		head.next = head;          //初始化的时候，自己指向自己
		iCount = 0;
	}
	
	//判断表是否为空
	public boolean isEmpty() {
		return iCount == 0 ? true : false;
	}
	
	//获取元素个数
	public int size() {
		return iCount;
	}
	
	//添加数据（头添加）
	public void addFromHead(T e) {	
		Node node = new Node(e);
		if (isEmpty()) //如果链表为空，则尾指针指向第一次添加进来的节点
			tail.next = node;
		node.next = head.next;
		head.next = node;
		++iCount;
	}
	
	//添加数据（尾添加）
	public void addFromTail(T e) {		
		if (isEmpty())                      //如果链表为空则直接调用AddFromHead，放置下一条语句引用空指针
			addFromHead(e);
		else {
			Node node = new Node(e);
			tail.next.next = node;          //直接把新的节点挂在链表末尾
			node.next = head;				//最后一个节点指向head
			tail.next = node;				//tail指向最后一个节点
			++iCount;
		}
	}
	
	//插入数据（指定位置）
	public void insert(int local, T e) {	
		if (local < iCount && local >= 0) {
			Node node = new Node(e);
			Node cur = head;   //注意，这里千万不能是m_head->next,因为如果当前链表为空插入的位置
			   //是0，则会直接执行cur->next这个表达式，解引用空指针会出错	
			for (int i = 0;i < local;i++, cur = cur.next); //遍历到local这个位置
			node.next = cur.next;
			cur.next = node;       //这里的操作就和头添加一样,就是改变指向
			++iCount;
		} else if(local == iCount)//如果是插入尾部，直接调用AddFromTail函数即可
			addFromTail(e);
	}
	
	//删除数据 （指定位置）
	public void deleteFromLocal(int local) {		
		if (local < iCount && local >= 0) {//当删除的位置合法的时候才进行删除操作
			Node pre = head;
			Node cur = head.next;
			for (int i = 0;i < local;i++, cur = cur.next, pre = pre.next);  //遍历到指定位置
			pre.next = cur.next;  //例如:pre->cur->next,现在直接让pre->next
			--iCount;
		}
	}
	
	//删除数据 （指定元素）
	public void deleteFromElement(T e) {			
		Node pre = head;
		Node cur = head.next;
		for (int i = 0;i < iCount;i++, cur = cur.next, pre = pre.next)  //遍历到指定位置
			if (cur.data == e) {
				pre.next = cur.next;  //例如:pre->cur->next,现在直接让pre->next
				--iCount;
				break;
			}
	}
	
	//根据指定元素查找，返回位置
	public int searchFromElmemt(T e) {				
		return getLocal(e);
	}
	
	//根据位置查找指定元素  
	public T searchFromLocal(int local) throws Exception {		
		if (local < iCount && local >= 0) {//当搜索的位置合法的时候才进行搜索操作
			Node cur = head.next;
			for (int i = 0; i < local; i++, cur = cur.next);
			return cur.data;
		} else
			throw new Exception("查找的位置不存在，查找操作失败.");
	}
	
	////逆转表(方法1，牺牲时间换取空间，速度很慢，时间复杂度为0(n^2))
	public void reverse1() {						
		T temp;
		for (int i = 0;i < iCount / 2;i++) {
			Node node1 = getNode(i);                   //获取节点
			Node node2 = getNode(iCount - i - 1);
			temp = node1.data;                         //交换数据项，千万别交换节点
			node1.data = node2.data;
			node2.data = temp; 
		}
	}
	
	//逆转表(方法2，牺牲空间换取时间，速度很快，时间复杂度为0(n))
	public void reverse2() {
		Node tHead = new Node(null);     //临时头节点
		Node tTail = new Node(null);     //临时尾指针
		tHead.next = tHead;
		Node cur = head.next;
		for (int i = 0;i < iCount;i++) {
			Node node = new Node(cur.data);
			if(i == 0) {
				tTail.next = node;           //一开始就让tTail指向最后一个节点		 
				node.next = tHead;			 //最后一个节点指向头节点
			}
			node.next = tHead.next;			  //这里采用头添加，结束后，就会自动逆转过来了         
			tHead.next = node;
			cur = cur.next;
		}
		head = tHead;   //因为头添加已经是逆转了，所以直接把头节点和尾节点的地址赋值给成员变量即可
		tail = tTail;   //注意，一定要头尾同时赋值，不然它们的指向就乱了。
	}
	
	//遍历元素
	public void print() {
		if(isEmpty())   //如果链表为空，则不做任何操作，防止空指针异常
			return;
		Node cur = head.next;
		for (int i = 0; i < iCount; i++,cur = cur.next) {
			System.out.println(cur.data);
		}
	}
	
	//复制表
	public CycleLinkList<T> copyList() {	//不能直接用clone方法，必须要一个个节点复制
		CycleLinkList<T> list = new CycleLinkList<T>();
		Node cur = head.next;
		for (int i = 0; i < iCount; i++,cur = cur.next) {
			list.addFromTail(cur.data);  //头添加效率高
		}
		return list;
	}
	
	//修改元素
	public void rewrite(int local, T e) throws Exception {			
		if (local < iCount && local >= 0) {
			Node cur = head.next;
			for (int i = 0;i < local;i++, cur = cur.next);
			cur.data = e;
		} else {
			throw new Exception("修改的位置不存在，修改操作失败.");
		}
	}
	
	//清空链表
	public void clearLinkList() {                   
		head.next = head;       //因为java有垃圾回收机制，所以不需要手动释放节点，只要让头节点指向自己,尾指针为空就好
		tail.next = null;
		iCount = 0;
	}
	
	//获取指定位置的节点
	public Node getNode(int local) {            
		if (local < iCount && local >= 0) {//当获取的位置合法的时候才进行获取操作
			Node cur = head.next;
			for (int i = 0;i < local;i++, cur = cur.next);
			return cur;
		} else
			return null;
	}
	
	//获取最后一个节点
	public Node getTailNode() {                 
		return tail.next;
	}
	
	//返回元素位置
	private int getLocal(T e) { 						
		Node cur = head.next;
		for (int i = 0;i < iCount;i++, cur = cur.next)
			if (cur.data == e)
				return i;
		return -1;
	}
	
	//升序
	public void sortByAsc() {
		selectSort(true);
	}
	
	//降序
	public void sortByDesc() {
		selectSort(false);
	}
	
	//选择排序法
	private void selectSort(boolean bAsc) {
		for (int i = 0;i < iCount;i++) {
			Node node1 = getNode(i);
			for (int j = i + 1;j<iCount;j++) {
				Node node2 = getNode(j);
				T temp;
				if (bAsc) {//升序
					if ((int)node1.data > (int)node2.data) {
						temp = node1.data;
						node1.data = node2.data;
						node2.data = temp;
					}
				} else  {//降序
					if ((int)node1.data < (int)node2.data) {
						temp = node1.data;
						node1.data = node2.data;
						node2.data = temp;
					}
				}
			}
		}
	}
	//合并链表  ,需要注意的是，合并后，原来的表就会被改变。
	public void mergeList(CycleLinkList<T> sl) {
		tail.next.next = sl.head.next;     //表1的最后一个节点指向表2的第一个节点
		tail.next = sl.tail.next;           //表1的尾指针指向表二的最后一个元素 
		sl.tail.next.next = head;			//表2的最后一个节点指向表1的head节点
		iCount += sl.iCount;                  //还要把元素个数加起来
	}

	//合并链表(有序)
	public void mergeListOrder(CycleLinkList<T> sl,boolean bAsc) {
		mergeList(sl);
		selectSort(bAsc);
	}

	//合并链表(无序)，不会改变原表
	void mergeListNoChange(CycleLinkList<T> sl) {
		CycleLinkList<T> list = new CycleLinkList<T>();
		list = sl.copyList();            //复制链表,这样就不会改变原表
		mergeList(list);          
	}

	//合并链表(有序),不会改变原表
	void mergeListNoChangeOrder(CycleLinkList<T> sl, boolean bAsc) {
		CycleLinkList<T> list = new CycleLinkList<T>();
		list = sl.copyList();           //复制链表,这样就不会改变原表
		mergeListOrder(list, bAsc);
	}
}
