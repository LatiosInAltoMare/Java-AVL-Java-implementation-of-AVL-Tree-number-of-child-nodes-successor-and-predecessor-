# Java 实现AVL树
无递归的完整AVL树，全部使用迭代

各方法声明：
节点类：
```
static class Node{
    Node father;
    Node left;
    Node right;
    int height;
    int NumberOfNodes;
    int key;
    public Node(int key){
        this.father=null;
        this.left=null;
        this.right=null;
        this.height=0;
        this.key=key;
        NumberOfNodes=0;
    }
}
```
树构造器：
```
public AVL_Tree(Node root){
    if(root!=null) {
        this.root = root;
        root.height = -1;
        root.NumberOfNodes = 0;
    }
}//树的唯一一个参数是'''Node root'''
```
添加节点：```public void add(Node CurrentNode)```
删除节点：```public void delete(int CurrentNumber)```
寻找前驱节点：```public Node FindNext(int CurrentNumber)```
寻找后继节点：```public Node FindPrev(int CurrentNumber)```

祝你使用愉快！:)
