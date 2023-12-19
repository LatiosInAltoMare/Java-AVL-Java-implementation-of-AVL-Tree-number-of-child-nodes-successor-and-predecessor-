public class Main {
    public static void main(String[] args) {
        //TODO: your code here !
    }

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
    static class AVL_Tree{
        Node root;
        public AVL_Tree(Node root){
            if(root!=null) {
                this.root = root;
                root.height = -1;
                root.NumberOfNodes = 0;
            }
        }//Part1 添加节点
        public void add(Node CurrentNode){//先按照普通的添加节点的方式，然后向上更新树高
            Node CurrentRoot=this.root;
            if(this.root==null){
                this.root=CurrentNode;
                return;
            }
            Node Point=CurrentRoot;
            while(true){
                if(Point.left!=null&&CurrentNode.key<Point.key){
                    Point=Point.left;
                }else if(Point.right!=null&&CurrentNode.key>=Point.key){
                    Point=Point.right;
                }else if(Point.left==null&&CurrentNode.key<Point.key){
                    Point.left=CurrentNode;
                    CurrentNode.father=Point;
                    break;
                }else {
                    Point.right=CurrentNode;
                    CurrentNode.father=Point;
                    break;
                }
            }//往上更新树高，新节点一定是叶节点
            UpdateHeight(Point);//接下来检查平衡性，并进行旋转操作，分四种情况，从CurrentPoint开始，向上寻找
            Point=CurrentNode.father;
            while(Point!=null){
                Fix_AVL(Point);
                Point=Point.father;
            }
        }//Part2 删除节点
        public void delete(int CurrentNumber){//首先必须找到CurrentNumber对应的节点public void delete(AVL_Tree CurrentTree,int CurrentNumber)
            Node CurrentNode=this.root;
            if(CurrentNode==null){
                return;
            }else if(CurrentNode.left==null&&CurrentNode.right==null){
                this.root=null;
                return;
            }else if(CurrentNode.left==null&&CurrentNode.NumberOfNodes==1){//左边是空
                if(CurrentNumber==CurrentNode.key){
                    this.root=CurrentNode.right;
                    CurrentNode.right.father=null;
                    CurrentNode.right.height=0;
                    CurrentNode.right.NumberOfNodes=0;
                }else{
                    CurrentNode.right=null;
                    CurrentNode.height=0;
                    CurrentNode.NumberOfNodes=0;
                }
                return;
            }else if(CurrentNode.right==null&&CurrentNode.NumberOfNodes==1){//右边是空
                if(CurrentNumber==CurrentNode.key){
                    this.root=CurrentNode.left;
                    CurrentNode.left.father=null;
                    CurrentNode.left.height=0;
                    CurrentNode.left.NumberOfNodes=0;
                }else{
                    CurrentNode.left=null;
                    CurrentNode.height=0;
                    CurrentNode.NumberOfNodes=0;
                }
                return;
            }
            while(true){
                if(CurrentNode.key==CurrentNumber){
                    break;
                }else if(CurrentNode.key>CurrentNumber){
                    CurrentNode=CurrentNode.left;
                }else{
                    CurrentNode=CurrentNode.right;
                }
            }//先像处理普通二叉树一样
            if(CurrentNode.left==null&&CurrentNode.right==null){//是叶节点
                Node CurrentFather=CurrentNode.father;
                if(CurrentFather.left==CurrentNode){
                    CurrentFather.left=null;
                }else{
                    CurrentFather.right=null;
                }
                Node Point=CurrentFather;
                UpdateHeight(Point);
                while(Point!=null){
                    Fix_AVL(Point);
                    Point=Point.father;
                }
            }else if(CurrentNode.left!=null&&CurrentNode.right==null){//只有左子树。如果只有左子树，那么直接接上
                Node CurrentFather=CurrentNode.father;
                if(CurrentFather.left==CurrentNode){
                    CurrentFather.left=CurrentNode.left;
                }else{
                    CurrentFather.right=CurrentNode.left;
                }
                CurrentNode.left.father=CurrentFather;
                Node Point=CurrentFather;
                UpdateHeight(Point);
                while(Point!=null){
                    Fix_AVL(Point);
                    Point=Point.father;
                }
            }else if(CurrentNode.left == null){//只有右子树
                Node CurrentFather=CurrentNode.father;
                if(CurrentFather.left==CurrentNode){
                    CurrentFather.left=CurrentNode.right;
                }else{
                    CurrentFather.right=CurrentNode.right;
                }
                CurrentNode.right.father=CurrentFather;
                Node Point=CurrentFather;
                UpdateHeight(Point);
                while(Point!=null){
                    Fix_AVL(Point);
                    Point=Point.father;
                }
            }else{//有左子树和右子树，需要找到后继节点
                Node Point=CurrentNode;
                while(true){//后继节点一定只有右子树或者是叶子
                    if(Point.left==null){
                        break;
                    }else if(Point.right == null){
                        Point=Point.left;
                    }else {
                        if(Point.left.key>=CurrentNode.key){
                            Point=Point.left;
                        }else if(CurrentNode.key<=Point.right.key){
                            Point=Point.right;
                        }
                    }
                }//Point就是后继节点。交换Point和CurrentNode两个节点
                int temp=Point.key;
                Point.key=CurrentNode.key;
                CurrentNode.key=temp;//接下来删除Point节点
                Node CurrentFather=Point.father;
                if(CurrentFather.left==Point){
                    CurrentFather.left= Point.right;
                }else{
                    CurrentFather.right= Point.right;
                }
                if(Point.right!=null){
                    Point.right.father=CurrentFather;
                }
                Point = CurrentFather;
                UpdateHeight(Point);
                while(Point!=null){
                    Fix_AVL(Point);
                    Point=Point.father;
                }
            }

        }//辅助方法：对不平衡节点修复AVL树
        public void Fix_AVL(Node ImbalanceNode){//如果左右子树不为空，则直接判断高度决定
            if(ImbalanceNode.left!=null&&ImbalanceNode.right!=null){
                if(ImbalanceNode.left.height-ImbalanceNode.right.height==2){
                    if(ImbalanceNode.left.left.height-ImbalanceNode.left.right.height>=0){
                        KeepBalanceLL(ImbalanceNode);
                    }else{
                        KeepBalanceLR(ImbalanceNode);
                    }
                }else if(ImbalanceNode.right.height-ImbalanceNode.left.height==2){
                    if(ImbalanceNode.right.right.height-ImbalanceNode.right.left.height>=0){
                        KeepBalanceRR(ImbalanceNode);
                    }else{
                        KeepBalanceRL(ImbalanceNode);
                    }
                }
            }else if(ImbalanceNode.left != null){//如果左子树不为空，右子树为空，左左（需要判断B是否为空）或左右
                if(ImbalanceNode.left.left!=null){
                    KeepBalanceLL(ImbalanceNode);
                }else{
                    KeepBalanceLR(ImbalanceNode);
                }
            }else if(ImbalanceNode.right!=null){//如果右子树不为空，左子树为空
                if(ImbalanceNode.right.right!=null){
                    KeepBalanceRR(ImbalanceNode);
                }else{
                    KeepBalanceRL(ImbalanceNode);
                }
            }
        }//辅助方法：向上更新树高（Point是要更新的起始点）
        public void UpdateHeight(Node Point){
            while(Point!=null){//当前树高=Max{左数高，右树高}+1，如果没有再更改过，则退出
                if(Point.left==null&&Point.right==null){
                    Point.NumberOfNodes=0;
                    Point.height=0;
                } else if(Point.left!=null&&Point.right!=null){
                    Point.height=Math.max(Point.left.height,Point.right.height)+1;
                    Point.NumberOfNodes=Point.left.NumberOfNodes+Point.right.NumberOfNodes+2;
                }else if(Point.left!=null){//右边是空
                    Point.height=Point.left.height+1;
                    Point.NumberOfNodes=Point.left.NumberOfNodes+1;
                }else{
                    Point.height=Point.right.height+1;
                    Point.NumberOfNodes=Point.right.NumberOfNodes+1;
                }
                Point=Point.father;
            }
        }//辅助方法：处理四种不平衡情况
        public void KeepBalanceLL(Node ImbalanceNode){//ImbalanceNode.left.right和ImbalanceNode.right可能是空
            if(ImbalanceNode.left==null){
                return;
            }
            Node b=ImbalanceNode.left;
            Node TempFather;
            if(ImbalanceNode.father==null){//ImbalanceNode是根节点，此时需要替换根节点
                this.root=b;
                b.father=null;
            }else if(ImbalanceNode.father.left==ImbalanceNode){//ImbalanceNode是父节点的左孩子
                TempFather= ImbalanceNode.father;
                TempFather.left=b;
                b.father=TempFather;
            }else{//ImbalanceNode是父节点的右孩子
                TempFather= ImbalanceNode.father;
                TempFather.right=b;
                b.father=TempFather;
            }
            ImbalanceNode.left=b.right;
            b.right= ImbalanceNode;
            ImbalanceNode.father=b;
            if(ImbalanceNode.left!=null){
                ImbalanceNode.left.father= ImbalanceNode;
            }
            if(ImbalanceNode.right==null){//B是空的或者B C树高一致
                if(ImbalanceNode.left==null){
                    ImbalanceNode.height=0;
                    ImbalanceNode.NumberOfNodes=0;
                }else{
                    ImbalanceNode.height=1;
                    ImbalanceNode.NumberOfNodes=1;
                }
                UpdateHeight(b);
            }else{
                UpdateHeight(ImbalanceNode);
            }
        }
        public void KeepBalanceLR(Node ImbalanceNode){
            if(ImbalanceNode.left==null||ImbalanceNode.left.right==null){
                return;
            }
            Node b=ImbalanceNode.left;
            Node c=ImbalanceNode.left.right;
            Node TempFather;
            if(ImbalanceNode.father==null){//ImbalanceNode是根节点，此时需要替换根节点
                this.root=c;
                c.father=null;
            }else if(ImbalanceNode.father.left==ImbalanceNode){//ImbalanceNode是父节点的左孩子
                TempFather= ImbalanceNode.father;
                TempFather.left=c;
                c.father=TempFather;
            }else{//ImbalanceNode是父节点的右孩子
                TempFather= ImbalanceNode.father;
                TempFather.right=c;
                c.father=TempFather;
            }
            b.right=c.left;
            if(b.right!=null){
                b.right.father=b;
            }
            b.father=c;
            c.left=b;
            ImbalanceNode.left=c.right;
            if(ImbalanceNode.left!=null){
                ImbalanceNode.left.father= ImbalanceNode;
            }
            c.right= ImbalanceNode;
            ImbalanceNode.father=c;
            b.height--;
            ImbalanceNode.height-=2;
            if(b.left==null&&b.right==null&& ImbalanceNode.left==null&& ImbalanceNode.right==null){//四大皆空
                b.NumberOfNodes=0;
                ImbalanceNode.NumberOfNodes=0;
            }else if(b.right==null){
                b.NumberOfNodes=1;
                ImbalanceNode.NumberOfNodes=2;
            }else if(ImbalanceNode.left==null){
                b.NumberOfNodes=2;
                ImbalanceNode.NumberOfNodes=1;
            }else{
                b.NumberOfNodes-=(ImbalanceNode.left.NumberOfNodes+2);
                ImbalanceNode.NumberOfNodes-=(b.left.NumberOfNodes+b.right.NumberOfNodes+4);
            }
            UpdateHeight(c);
        }
        public void KeepBalanceRL(Node ImbalanceNode){
            if(ImbalanceNode.right==null||ImbalanceNode.right.left==null){
                return;
            }
            Node b=ImbalanceNode.right;
            Node c=ImbalanceNode.right.left;
            Node TempFather;
            if(ImbalanceNode.father==null){//ImbalanceNode是根节点，此时需要替换根节点
                this.root=c;
                c.father=null;
            }else if(ImbalanceNode.father.left==ImbalanceNode){//ImbalanceNode是父节点的左孩子
                TempFather= ImbalanceNode.father;
                TempFather.left=c;
                c.father=TempFather;
            }else{//ImbalanceNode是父节点的右孩子
                TempFather= ImbalanceNode.father;
                TempFather.right=c;
                c.father=TempFather;
            }
            b.left=c.right;
            if(b.left!=null){
                b.left.father=b;
            }
            b.father=c;
            c.right=b;
            ImbalanceNode.right=c.left;
            if(ImbalanceNode.right!=null){
                ImbalanceNode.right.father= ImbalanceNode;
            }
            c.left= ImbalanceNode;
            ImbalanceNode.father=c;
            b.height--;
            ImbalanceNode.height-=2;
            if(b.left==null&&b.right==null&& ImbalanceNode.left==null&& ImbalanceNode.right==null){//四大皆空
                b.NumberOfNodes=0;
                ImbalanceNode.NumberOfNodes=0;
            }else if(b.left==null){
                b.NumberOfNodes=1;
                ImbalanceNode.NumberOfNodes=2;
            }else if(ImbalanceNode.right==null){
                b.NumberOfNodes=2;
                ImbalanceNode.NumberOfNodes=1;
            }else{
                b.NumberOfNodes-=(ImbalanceNode.right.NumberOfNodes+2);
                ImbalanceNode.NumberOfNodes-=(b.right.NumberOfNodes+b.left.NumberOfNodes+4);
            }
            UpdateHeight(c);
        }
        public void KeepBalanceRR(Node ImbalanceNode){
            if(ImbalanceNode.right==null){
                return;
            }
            Node b=ImbalanceNode.right;
            Node TempFather;
            if(ImbalanceNode.father==null){//ImbalanceNode是根节点，此时需要替换根节点
                this.root=b;
                b.father=null;
            }else if(ImbalanceNode.father.left==ImbalanceNode){//ImbalanceNode是父节点的左孩子
                TempFather= ImbalanceNode.father;
                TempFather.left=b;
                b.father=TempFather;
            }else{//ImbalanceNode是父节点的右孩子
                TempFather= ImbalanceNode.father;
                TempFather.right=b;
                b.father=TempFather;
            }
            ImbalanceNode.right=b.left;
            b.left= ImbalanceNode;
            ImbalanceNode.father=b;
            if(ImbalanceNode.right!=null){
                ImbalanceNode.right.father= ImbalanceNode;
            }
            if(ImbalanceNode.left==null){//B是空的或者B C树高一致
                if(ImbalanceNode.right==null){
                    ImbalanceNode.height=0;
                    ImbalanceNode.NumberOfNodes=0;
                }else{
                    ImbalanceNode.height=1;
                    ImbalanceNode.NumberOfNodes=1;
                }
                UpdateHeight(b);
            }else{
                UpdateHeight(ImbalanceNode);
            }
        }//辅助方法：找到后继和前驱节点
        public Node FindNext(int CurrentNumber){
            Node Point=this.root;
            Node TempPoint=null;
            while(true){//后继节点一定只有右子树或者是叶子 什么时候可以确定是它？左边没有，右边有
                if(Point.left==null&&Point.key>=CurrentNumber){
                    return Point;
                }else if(Point.left==null){
                    if(Point.right!=null){
                        Point=Point.right;
                    }else{
                        return TempPoint;
                    }
                }else if(Point.right==null){
                    if(Point.key<CurrentNumber){
                        return TempPoint;
                    }else{
                        TempPoint=Point;
                        Point=Point.left;
                    }
                }else{//左右都有 比左小，则往左（记TempPoint），比右大，则往右，两者中间，则往右（记TempPoint）
                    if(Point.key>=CurrentNumber){//当前节点较大，不可能比右大
                        if(CurrentNumber<=Point.left.key){//比左小
                            TempPoint=Point.left;
                        }else{//介于left和right之间
                            TempPoint=Point;
                        }
                        Point=Point.left;
                    }else{//当前节点较小
                        if (CurrentNumber <= Point.right.key) {
                            TempPoint = Point.right;
                        }
                        Point=Point.right;
                    }
                }
            }
        }
        public Node FindPrev(int CurrentNumber){
            Node Point=this.root;
            Node TempPoint=null;
            while(true){//什么时候可以确定？
                if(Point.right==null&&Point.key<=CurrentNumber){
                    return Point;
                }else if(Point.right==null){
                    if(Point.left!=null){
                        Point=Point.left;
                    }else{
                        return TempPoint;
                    }
                }else if(Point.left==null){
                    if(Point.key>CurrentNumber){
                        return TempPoint;
                    }else{
                        TempPoint = Point;
                        Point=Point.right;
                    }
                }else{
                    if(Point.key<=CurrentNumber){
                        if(CurrentNumber>=Point.right.key){
                            TempPoint=Point.right;
                        }else{
                            TempPoint=Point;
                        }
                        Point=Point.right;
                    }else{
                        if(CurrentNumber>=Point.left.key){
                            TempPoint=Point.left;
                        }
                        Point=Point.left;
                    }
                }
            }
        }
    }
}
