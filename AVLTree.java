import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * AVLTree
 *
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 *
 */

public class AVLTree {

    /**
     * public boolean empty()
     * <p>
     * Returns true if and only if the tree is empty.
     */
    public IAVLNode root;
    public IAVLNode virtaul_node = new AVLNode(-1, null);
    public IAVLNode min_node;
    public IAVLNode max_node;

    public boolean empty() {
        if (root == null) {
            return true;
        }
        return false;
    }

    /**
     * public String search(int k)
     * <p>
     * Returns the info of an item with key k if it exists in the tree.
     * otherwise, returns null.
     */
    public String search(int k) {
        IAVLNode r = root;
        if (root == null) return null;
        while (r.isRealNode()) {
            if (r.getKey() == k) return r.getValue();
            if (r.getKey() > k) {
                r = r.getLeft();
                continue;
            } else {
                r = r.getRight();
                continue;
            }
        }
        return null;
    }

    /**
     * rightRotate
     * The function does not update the height.
     */
    public void rightRotate(IAVLNode A, IAVLNode B) {
        IAVLNode Bparent = B.getParent();
        if (Bparent != null) {
            if (Bparent.getLeft() == B) Bparent.setLeft(A);
            else Bparent.setRight(A);
        } else this.root = A;
        A.setParent(Bparent);
        IAVLNode ar = A.getRight();
        ar.setParent(B);
        A.setRight(B);
        B.setParent(A);
        B.setLeft(ar);
        B.setsize(ar.getsize(), B.getsizeright());
        A.setsize(A.getsizeleft(), B.getsize());
    }

    public void leftRotate(IAVLNode A, IAVLNode B) {
        IAVLNode Bparent = B.getParent();
        if (Bparent != null) {
            if (Bparent.getLeft() == B) Bparent.setLeft(A);
            else Bparent.setRight(A);
        } else this.root = A;
        A.setParent(Bparent);
        IAVLNode al = A.getLeft();
        al.setParent(B);
        A.setLeft(B);
        B.setParent(A);
        B.setRight(al);
        B.setsize(B.getsizeleft(), al.getsize());
        A.setsize(B.getsize(), A.getsizeright());
    }

    public void leftrightRotate(IAVLNode A, IAVLNode B, IAVLNode C) {
        IAVLNode Cparent = C.getParent();
        if (Cparent != null) {
            if (Cparent.getLeft() == C) Cparent.setLeft(B);
            else Cparent.setRight(B);
        } else this.root = B;
        B.setParent(Cparent);
        IAVLNode bl = B.getLeft();
        IAVLNode br = B.getRight();
        bl.setParent(A);
        br.setParent(C);
        A.setParent(B);
        C.setParent(B);
        B.setLeft(A);
        B.setRight(C);
        C.setLeft(br);
        A.setRight(bl);
        A.setsize(A.getsizeleft(), bl.getsize());
        C.setsize(br.getsize(), C.getsizeright());
        B.setsize(A.getsize(), C.getsize());
    }

    public void rightleftRotate(IAVLNode A, IAVLNode B, IAVLNode C) {
        IAVLNode Aparent = A.getParent();
        if (Aparent != null) {
            if (Aparent.getLeft() == A) {
                Aparent.setLeft(B);
            } else {
                Aparent.setRight(B);
            }
        } else this.root = B;
        B.setParent(Aparent);
        IAVLNode bl = B.getLeft();
        IAVLNode br = B.getRight();
        bl.setParent(A);
        br.setParent(C);
        A.setParent(B);
        C.setParent(B);
        B.setLeft(A);
        B.setRight(C);
        C.setLeft(br);
        A.setRight(bl);
        A.setsize(A.getsizeleft(), bl.getsize());
        C.setsize(br.getsize(), C.getsizeright());
        B.setsize(A.getsize(), C.getsize());
    }

    public int BF(IAVLNode v) {
        IAVLNode vleft = v.getLeft();
        IAVLNode vright = v.getRight();
        return vleft.getHeight() - vright.getHeight();
    }

    /**
     * public int insert(int k, String i)
     * <p>
     * Inserts an item with key k and info i to the AVL tree.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        if (this.search(k) != null) return -1;
        IAVLNode n = new AVLNode(k, i);
        n.setLeft(virtaul_node);
        n.setRight(virtaul_node);
        if (this.root == null) {
            this.root = n;
            this.min_node = n;
            this.max_node = n;
            return 0;
        }

        if (min_node.getKey() > k) this.min_node = n;
        if (max_node.getKey() < k) this.max_node = n;

        IAVLNode r = this.root;
        IAVLNode p = r;

        while (r.isRealNode()) {
            p = r;
            if (n.getKey() < r.getKey()) {
                r = r.getLeft();
                continue;
            }
            if (n.getKey() > r.getKey()) {
                r = r.getRight();
                continue;
            }
        }
        n.setParent(p);
        if (n.getKey() < p.getKey()) p.setLeft(n);
        else p.setRight(n);

        r = this.root;
        p = n.getParent();
        int bf;
        IAVLNode p_left;
        IAVLNode p_right;
        int h;
        int result = 0;
        IAVLNode p_double_rotate;

        while (true) {
            bf = this.BF(p);
            p_left = p.getLeft();
            p_right = p.getRight();

            if (bf == 2) {
                if ((p_left.isRealNode()) && (this.BF(p_left) == 1)) {
                    this.rightRotate(p_left, p);
                    h = p.getHeight();
                    p.setHeight(h - 1);
                    result = 1;
                    if (p == r) break;
                    p = p.getParent();
                    continue;
                }

                if ((p_left.isRealNode()) && (this.BF(p_left) == -1)) {
                    p_double_rotate = p_left.getRight();
                    this.leftrightRotate(p_left, p_double_rotate, p);
                    h = p_double_rotate.getHeight();
                    p_double_rotate.setHeight(h + 1);
                    h = p_left.getHeight();
                    p_left.setHeight(h - 1);
                    h = p.getHeight();
                    p.setHeight(h - 1);
                    result = 2;
                    if (p == r) break;
                    p = p.getParent();
                    continue;
                }
            }
            if (bf == -2) {
                if ((p_right.isRealNode()) && (this.BF(p_right) == -1)) {
                    this.leftRotate(p_right, p);
                    h = p.getHeight();
                    p.setHeight(h - 1);
                    result = 1;
                    if (p == r) break;
                    p = p.getParent();
                    continue;
                }

                if ((p_right.isRealNode()) && (this.BF(p_right) == 1)) {
                    p_double_rotate = p_right.getLeft();
                    this.rightleftRotate(p, p_double_rotate, p_right);
                    h = p_double_rotate.getHeight();
                    p_double_rotate.setHeight(h + 1);
                    h = p_right.getHeight();
                    p_right.setHeight(h - 1);
                    h = p.getHeight();
                    p.setHeight(h - 1);
                    result = 2;
                    if (p == r) break;
                    p = p.getParent();
                    continue;
                }

            }
            p.setsize(p_left.getsize(), p_right.getsize());
            p.setHeight(1 + Math.max(p.getLeft().getHeight(), p.getRight().getHeight()));
            if (p == r) break;
            p = p.getParent();
        }
        return result;


    }


    /**
     * public int delete(int k)
     * <p>
     * Deletes an item with key k from the binary tree, if it is there.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        if (this.search(k) == null) return -1;
        IAVLNode y = this.root;
        IAVLNode Y = this.root;
        IAVLNode p;
        IAVLNode r;
        IAVLNode y_Left;
        IAVLNode y_Right;

        if (y.getsize() == 1) {
            min_node = virtaul_node;
            max_node = virtaul_node;
            this.root = null;
            return 0;
        }

        while (y != null) //search for the node
        {
            if (y.getKey() == k) break;
            if (y.getKey() < k) y = y.getRight();
            else y = y.getLeft();
        }

        if (this.max_node == y) this.max_node = this.predecessor(y);//update the max if the node we delete is the max
        if (this.min_node == y) this.min_node = this.successor(y);//update the min if the node we delete is the min

        if ((!y.getLeft().isRealNode()) && (!y.getRight().isRealNode())) {// y does not have kids
            if (y.getParent().getLeft() == y) y.getParent().setLeft(virtaul_node);
            else y.getParent().setRight(virtaul_node);
            y.getParent().setsize(y.getParent().getLeft().getsize(), y.getParent().getRight().getsize());
            y.getParent().setHeight(1 + Math.max(y.getParent().getLeft().getHeight(), y.getParent().getRight().getHeight()));
            Y = y.getParent();

        }

        if ((y.getLeft().isRealNode()) && (!y.getRight().isRealNode())) {//y has only one kid
            if (y.getParent() == null) {
                this.root = y.getLeft();
            } else {
                if (y.getParent().getLeft() == y) y.getParent().setLeft(y.getLeft());
                else y.getParent().setRight(y.getLeft());
                y.getParent().setHeight(1 + Math.max(y.getParent().getLeft().getHeight(), y.getParent().getRight().getHeight()));
                y.getParent().setsize(y.getParent().getLeft().getsize(), y.getParent().getRight().getsize());
            }
            y.getLeft().setParent(y.getParent());
            Y = y.getParent();
        }

        if ((!y.getLeft().isRealNode()) && (y.getRight().isRealNode())) {//y has only one kid
            if (y.getParent() == null) {
                this.root = y.getRight();
            } else {
                if (y.getParent().getLeft() == y) y.getParent().setLeft(y.getRight());
                else y.getParent().setRight(y.getRight());
                y.getParent().setHeight(1 + Math.max(y.getParent().getLeft().getHeight(), y.getParent().getRight().getHeight()));
                y.getParent().setsize(y.getParent().getLeft().getsize(), y.getParent().getRight().getsize());
            }
            y.getRight().setParent(y.getParent());
            Y = y.getParent();
        }

        if ((y.getLeft().isRealNode()) && (y.getRight().isRealNode())) {//y has two kids
            p = this.successor(y);
            if (!p.getRight().isRealNode()) {//p does not have kids (it is not possible to have left kid
                if (p.getParent().getLeft() == p) {
                    p.getParent().setLeft(virtaul_node);
                    p.getParent().setsize(0, p.getParent().getsizeright());
                } else {
                    p.getParent().setRight(virtaul_node);
                    p.getParent().setsize(p.getParent().getsizeleft(), 0);
                }

            } else {//p has one kid
                if (p.getParent().getLeft() == p) {
                    p.getParent().setLeft(p.getRight());
                } else p.getParent().setRight(p.getRight());
                p.getParent().setsize(p.getParent().getLeft().getsize(), p.getParent().getRight().getsize());
                p.getRight().setParent(p.getParent());
            }


            r = p.getParent();

            if (y.getParent() == null) this.root = p;
            else {
                if (y.getParent().getLeft() == y) y.getParent().setLeft(p);
                else y.getParent().setRight(p);
            }
            if (y.getLeft().isRealNode()) y.getLeft().setParent(p);
            if (y.getRight().isRealNode()) y.getRight().setParent(p);
            p.setParent(y.getParent());
            p.setRight(y.getRight());
            p.setLeft(y.getLeft());
            Y = p;
            if (r != y) Y = r;

        }

        r = this.root;
        y = Y;
        int bf;
        int result = 0;
        IAVLNode p_double_rotate;
        if (y == null) return result;

        while (true) {
            bf = this.BF(y);
            y_Left = y.getLeft();
            y_Right = y.getRight();
            y.setsize(y_Left.getsize(), y_Right.getsize());

            if (bf == 2) {
                if ((y_Left.isRealNode()) && ((this.BF(y_Left) == 1 || this.BF(y_Left) == 0))) {
                    this.rightRotate(y_Left, y);
                    y.setHeight(1 + Math.max(y.getRight().getHeight(), y.getLeft().getHeight()));
                    y_Left.setHeight(1 + Math.max(y_Left.getLeft().getHeight(), y_Left.getRight().getHeight()));
                    result += 1;
                    if (y == r) break;
                    y = y.getParent();
                    continue;
                }

                if ((y_Left.isRealNode()) && (this.BF(y_Left) == -1)) {
                    p_double_rotate = y_Left.getRight();
                    this.leftrightRotate(y_Left, p_double_rotate, y);
                    y.setHeight(1 + Math.max(y.getRight().getHeight(), y.getLeft().getHeight()));
                    y_Left.setHeight(1 + Math.max(y_Left.getLeft().getHeight(), y_Left.getRight().getHeight()));
                    p_double_rotate.setHeight(1 + Math.max(p_double_rotate.getLeft().getHeight(), p_double_rotate.getRight().getHeight()));
                    result += 2;
                    if (y == r) break;
                    y = y.getParent();
                    continue;
                }
            }
            if (bf == -2) {
                if ((y_Right.isRealNode()) && ((this.BF(y_Right) == -1) || (this.BF(y_Right) == 0))) {
                    this.leftRotate(y_Right, y);
                    y.setHeight(1 + Math.max(y.getRight().getHeight(), y.getLeft().getHeight()));
                    result += 1;
                    if (y == r) break;
                    y = y.getParent();
                    continue;
                }

                if ((y_Right.isRealNode()) && (this.BF(y_Right) == 1)) {
                    p_double_rotate = y_Right.getLeft();
                    this.rightleftRotate(y, p_double_rotate, y_Right);
                    y_Right.setHeight(1 + Math.max(y_Right.getLeft().getHeight(), y_Right.getRight().getHeight()));
                    y.setHeight(1 + Math.max(y.getLeft().getHeight(), y.getRight().getHeight()));
                    p_double_rotate.setHeight(1 + Math.max(p_double_rotate.getLeft().getHeight(), p_double_rotate.getRight().getHeight()));
                    result += 2;
                    if (y == r) break;
                    y = y.getParent();
                    continue;
                }

            }

            y.setHeight(1 + Math.max(y.getLeft().getHeight(), y.getRight().getHeight()));
            if (y == r) break;
            y = y.getParent();
        }
        return result;
    }


    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty.
     */
    public String min() {
        if (root == null) return null;
        return min_node.getValue();
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
     */
    public String max() {
        if (root == null) return null;
        return max_node.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        if (this.root == null) {
            return new int[0];
        }
        IAVLNode minnod = min_node;
        IAVLNode roo = root;
        int[] arr = new int[roo.getsize()];
        arr[0] = minnod.getKey();
        for (int i = 1; i < arr.length; i++) {
            IAVLNode ss = successor(minnod);
            arr[i] = ss.getKey();
            if (i < arr.length - 1) {
                minnod = ss;
            }
        }

        return arr;
    }

    /**
     * public IAVLNode successor(IAVLNode x)
     * <p>
     * x!=max
     * Searches and returns the successor.
     */
    public IAVLNode successor(IAVLNode x) //we wrote
    {
        if (x.getRight().isRealNode()) {
            // x = x.getRight();
            // while (x.getLeft().isRealNode()) x = x.getLeft();
            // return x;
            return EXTRAmin(x.getRight());
        }
        IAVLNode y = x.getParent();
        while (y.isRealNode() && x == y.getRight()) {
            x = y;
            y = x.getParent();
        }
        return y;
    }

    /**
     * public IAVLNode predecessor(IAVLNode x)
     * <p>
     * x!=min
     * Searches and returns the predecessor.
     */
    public IAVLNode predecessor(IAVLNode x)//we wrote
    {
        if (x.getLeft().isRealNode()) {
            // x = x.getLeft();
            // while (x.getRight().isRealNode())
            //   {
            //        x = x.getRight();
            //  }
            //  return x;
            return EXTRAmax(x.getLeft());
        }
        IAVLNode y = x.getParent();
        while (y != null && y.isRealNode() && y.getParent() != null && y.getParent().getLeft() == y) {
            x = y;
            y = x.getParent();
        }
        return y;
    }


    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        int[] brr = keysToArray();
        if (brr == new int[0]) {
            return new String[0];
        }
        IAVLNode roo = root;
        String[] arr = new String[roo.getsize()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = search(brr[i]);
        }
        return arr;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        if (this.root == null || !root.isRealNode()) {
            return 0;
        }
        IAVLNode roo = root;
        return roo.getsize();
    }


    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public IAVLNode getRoot() {
        if (this.root == null || !root.isRealNode()) {
            return null;
        }
        return this.root;
    }

    /**
     * public AVLTree[] split(int x)
     * <p>
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     * <p>
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */
    public IAVLNode EXTRAmax(IAVLNode x) {
        while (x.isRealNode() && x.getRight().isRealNode())//&& root.isRealNode()
        {
            x = x.getRight();
        }
        return x;
    }

    public IAVLNode EXTRAmin(IAVLNode x) {
        while (x.isRealNode() && x.getLeft().isRealNode())//&& root.isRealNode()
        {
            x = x.getLeft();
        }
        return x;
    }

    public AVLTree[] split(int x) {
        AVLTree[] s = new AVLTree[2];
        AVLTree t1 = new AVLTree();
        AVLTree t2 = new AVLTree();
        AVLTree treetes = new AVLTree();
        treetes.root=this.root;
        IAVLNode tes = treetes.root;
       treetes.root.setHeight(this.root.getHeight());
        treetes.root.setsize(this.root.getsizeleft(),this.root.getsizeright());
        if (x == EXTRAmax(root).getKey()) {
            delete(x);
            AVLTree A1 = new AVLTree();
            A1.root = root;
            A1.max_node = EXTRAmax(A1.root);
            A1.min_node = EXTRAmin(A1.root);
            s[0] = A1;
            s[1] = null;
            return s;
        }
        if (x == EXTRAmin(root).getKey()) {
            delete(x);
            AVLTree A1 = new AVLTree();
            A1.root = root;
            A1.max_node = EXTRAmax(A1.root);
            A1.min_node = EXTRAmin(A1.root);
            s[1] = A1;
            s[0] = null;
            return s;
        }
        if (x == root.getKey()) {
            AVLTree A1 = new AVLTree();
            AVLTree A2 = new AVLTree();
            A1.root = root.getLeft();
            A1.root.setParent(null);
            A2.root = root.getRight();
            A2.root.setParent(null);
            A1.max_node = EXTRAmax(A1.root);
            A1.min_node = EXTRAmin(A1.root);
            A2.max_node = EXTRAmax(A2.root);
            A2.min_node = EXTRAmin(A2.root);
            s[0] = A1;
            s[1] = A2;
            return s;
        }
        if (root.getLeft() != null && x == EXTRAmax(root.getLeft()).getKey()) {
            AVLTree A1 = new AVLTree();
            AVLTree A2 = new AVLTree();
            A1.root = root.getLeft();
            A1.root.setParent(null);
            A1.max_node = EXTRAmax(A1.root);
            A1.min_node = EXTRAmin(A1.root);
            A1.root.setHeight(root.getLeft().getHeight());
            A1.root.setsize(root.getLeft().getsizeleft(),root.getLeft().getsizeright());
            A1.delete(x);
            s[0] = A1;
            A2.root = root.getRight();
            A2.root.setParent(null);
            A2.max_node = EXTRAmax(A2.root);
            A2.min_node = EXTRAmin(A2.root);
            A2.root.setHeight(root.getRight().getHeight());
            A2.root.setsize(root.getRight().getsizeleft(),root.getRight().getsizeright());
            A2.insert(root.getKey(), root.getValue());
            s[1] = A2;
            return s;
        }
        if (root.getRight() != null && x == EXTRAmin(root.getRight()).getKey()) {
            AVLTree A1 = new AVLTree();
            AVLTree A2 = new AVLTree();
            A1.root = root.getLeft();
            A1.root.setParent(null);
            A1.max_node = EXTRAmax(A1.root);
            A1.min_node = EXTRAmin(A1.root);
            A1.root.setHeight(root.getLeft().getHeight());
            A1.root.setsize(root.getLeft().getsizeleft(),root.getLeft().getsizeright());
            A1.insert(root.getKey(), root.getValue());
            s[0] = A1;
            A2.root = root.getRight();
            A2.root.setParent(null);
            A2.max_node = EXTRAmax(A2.root);
            A2.min_node = EXTRAmin(A2.root);
            A2.root.setHeight(root.getRight().getHeight());
            A2.root.setsize(root.getRight().getsizeleft(),root.getRight().getsizeright());
            A2.delete(x);
            s[1] = A2;
            return s;
        }
        else
        {
            while (tes.getKey() != x)
            {
                if (tes.getKey() > x)
                {
                    tes = tes.getLeft();
                }
                else
                {
                    tes = tes.getRight();
                }
            }
            if (tes.getLeft().isRealNode())
            {
                t1.root = tes.getLeft();
                t1.root.setParent(null);
                t1.max_node = EXTRAmax(t1.root);
                t1.min_node = EXTRAmin(t1.root);
                t1.root.setHeight(tes.getLeft().getHeight());
                t1.root.setsize(tes.getLeft().getsizeleft(),tes.getLeft().getsizeright());
            }
            if (tes.getRight().isRealNode())
            {
                t2.root = tes.getRight();
                t2.root.setParent(null);
                t2.max_node = EXTRAmax(t2.root);
                t2.min_node = EXTRAmin(t2.root);
                t2.root.setHeight(tes.getRight().getHeight());
                t2.root.setsize(tes.getRight().getsizeleft(),tes.getRight().getsizeright());
            }

            while (tes.getParent() != null)
            {
                if (tes.getParent().getKey() > tes.getKey())
                {
                    AVLTree tree0 = new AVLTree();
                    tree0.root = tes.getParent().getRight();
                    tree0.root.setParent(null);
                    tree0.max_node = EXTRAmax(tree0.root);
                    tree0.min_node = EXTRAmin(tree0.root);
                    tree0.root.setHeight(tes.getParent().getRight().getHeight());
                    tree0.root.setsize(tes.getParent().getRight().getsizeleft(),tes.getParent().getRight().getsizeright());
                    t2.join(tes.getParent(), tree0);
                } else
                {
                    AVLTree tree0 = new AVLTree();
                    tree0.root = tes.getParent().getLeft();
                    tree0.root.setParent(null);
                    tree0.max_node = EXTRAmax(tree0.root);
                    tree0.min_node = EXTRAmin(tree0.root);
                    tree0.root.setHeight(tes.getParent().getLeft().getHeight());
                    tree0.root.setsize(tes.getParent().getLeft().getsizeleft(),tes.getParent().getLeft().getsizeright());
                    t1.join(tes.getParent(), tree0);
                }
                tes = tes.getParent();
            }
            s[0] = t1;
            s[1] = t2;
            return s;
        }
    }


    /**
     * public int join(IAVLNode x, AVLTree t)
     *
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     *
     * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t)
    {
        if ((this.root==null || !this.root.isRealNode())&&(t.root==null || !t.root.isRealNode())){this.root=x;t.root=x;return 0;}
        if ((this.root==null || !root.isRealNode()) && (t.root!=null && t.root.isRealNode())){t.insert(x.getKey(),x.getValue()); this.root=t.root;return t.root.getHeight()+1;}
        if ((t.root==null || !t.root.isRealNode()) && (this.root!=null&& this.root.isRealNode())){this.insert(x.getKey(),x.getValue());t.root=this.root; return this.root.getHeight()+1;}
        int res=Math.abs(this.root.getHeight()-t.root.getHeight())+1;
        IAVLNode lefttree;
        IAVLNode righttree;
        IAVLNode xx=x;

        if (this.root.getKey()>t.root.getKey())
        {
            righttree=this.root;
            lefttree=t.root;
        }
        else
        {
            righttree=t.root;
            lefttree=this.root;
        }
        x.setLeft(lefttree);
        x.setRight(righttree);
        x.setHeight(Math.max(lefttree.getHeight(),righttree.getHeight())+1);
        x.setsize(lefttree.getsize(),righttree.getsize());
        AVLTree lanatree=new AVLTree();
        lanatree.root=x;
        lanatree.root.setParent(null);
        lanatree.max_node=EXTRAmax(lanatree.root);
        lanatree.min_node=EXTRAmin(lanatree.root);
        IAVLNode t1 = righttree;
        IAVLNode t2 = lefttree;
        if (t2.getHeight() < t1.getHeight())
        {
            while (t1.getHeight() > t2.getHeight())
            {
                if (t1.getLeft().isRealNode())
                {
                    t1 = t1.getLeft();
                }
                else
                {
                    t1 = t1.getRight();
                }
            }
            xx=t1;
            IAVLNode t1par = t1.getParent();
            lanatree.root.setRight(t1);
            lanatree.root.setsize(lanatree.root.getsizeleft(),t1.getsize());
            lanatree.root.setHeight(Math.max(t1.getHeight(),lanatree.root.getLeft().getHeight())+1);
            t1par.setLeft(lanatree.root);
            t1par.setsize(lanatree.root.getsize(),t1par.getsizeright());
            t1par.setHeight(Math.max(lanatree.root.getHeight(),t1par.getRight().getHeight())+1);
            lanatree.root=t1par;

        }
        else if (t2.getHeight() > t1.getHeight())
        {
            while (t1.getHeight() < t2.getHeight())
            {
                if (t2.getRight().isRealNode())
                {
                    t2 = t2.getRight();
                }
                else
                {
                    t2 = t2.getLeft();
                }
            }
            xx=t2;
            IAVLNode t2par = t2.getParent();
            lanatree.root.setLeft(t2);
            lanatree.root.setsize(t2.getsize(),lanatree.root.getsizeright());
            lanatree.root.setHeight(Math.max(t2.getHeight(),lanatree.root.getRight().getHeight())+1);
            t2par.setRight(lanatree.root);
            t2par.setsize(t2par.getsizeleft(),lanatree.root.getsize());
            t2par.setHeight(Math.max(lanatree.root.getHeight(),t2par.getLeft().getHeight())+1);
            lanatree.root=t2par;
        }
        //gelgolem
        IAVLNode p=xx;
        int bf;
        IAVLNode p_left;
        IAVLNode p_right;
        int h;
        IAVLNode r=lanatree.root;
        IAVLNode p_double_rotate;
        while (true) {
            bf = this.BF(p);
            p_left = p.getLeft();
            p_right = p.getRight();

            if (bf==2)
            {
                if ((p_left.isRealNode())&&(this.BF(p_left)==1))
                {
                    this.rightRotate(p_left, p);
                    h = p.getHeight();
                    p.setHeight(h-1);
                    if(p==r) break;
                    p = p.getParent();
                    continue;
                }

                if ((p_left.isRealNode())&&(this.BF(p_left)==-1))
                {
                    p_double_rotate = p_left.getRight();
                    this.leftrightRotate(p_left, p_double_rotate, p) ;
                    h = p_double_rotate.getHeight();
                    p_double_rotate.setHeight(h+1);
                    h = p_left.getHeight();
                    p_left.setHeight(h-1);
                    h = p.getHeight();
                    p.setHeight(h-1);
                    if(p==r) break;
                    p = p.getParent();
                    continue;
                }
            }
            if (bf==-2)
            {
                if ((p_right.isRealNode())&&(this.BF(p_right)==-1))
                {
                    this.leftRotate(p_right, p);
                    h = p.getHeight();
                    p.setHeight(h-1);
                    if(p==r) break;
                    p = p.getParent();
                    continue;
                }

                if((p_right.isRealNode())&&(this.BF(p_right)==1))
                {
                    p_double_rotate = p_right.getLeft();
                    this.rightleftRotate(p, p_double_rotate, p_right);
                    h = p_double_rotate.getHeight();
                    p_double_rotate.setHeight(h+1);
                    h = p_right.getHeight();
                    p_right.setHeight(h-1);
                    h = p.getHeight();
                    p.setHeight(h-1);
                    if(p==r) break;
                    p = p.getParent();
                    continue;
                }

            }
            p.setsize(p_left.getsize(), p_right.getsize());
            p.setHeight(1+Math.max(p.getLeft().getHeight(), p.getRight().getHeight()));
            if(p==r) break;
            p = p.getParent();
        }
        this.root= lanatree.root;
        this.root.setHeight(lanatree.root.getHeight());
        this.root.setsize(lanatree.root.getsizeleft(),lanatree.root.getsizeright());
        t.root= lanatree.root;
        return res;
    }

    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode{
        public int getKey(); // Returns node's key (for virtual node return -1).
        public String getValue(); // Returns node's value [info], for virtual node returns null.
        public void setLeft(IAVLNode node); // Sets left child.
        public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
        public void setRight(IAVLNode node); // Sets right child.
        public IAVLNode getRight(); // Returns right child, if there is no right child return null.
        public void setParent(IAVLNode node); // Sets parent.
        public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
        public void setHeight(int height); // Sets the height of the node.
        public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
        public void setsize(int sl, int sr); // Setes the size of the sub-tree includes the node.
        public int getsize(); // Returns the size of the sub-tree.
        public int getsizeleft(); // Returns the size of the left-sub-tree.
        public int getsizeright(); // Returns the size of the right-sub tree.
    }

    /**
     * public class AVLNode
     *
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in another file.
     *
     * This class can and MUST be modified (It must implement IAVLNode).
     */
    public class AVLNode implements IAVLNode{
        private int key;
        private String info;
        private IAVLNode nodeL=null;
        private IAVLNode nodeR=null;
        private IAVLNode parent=null;
        private int sizeleft = 0;
        private int sizeright = 0;
        private int size=1;
        private int height =0;

        public AVLNode(int key,String info)
        {
            this.key=key;
            this.info=info;
            if (key==-1) {
                this.size = 0;
                this.height = -1;
            }
        }
        public int getKey()
        {
            return this.key;
        }
        public String getValue()
        {
            return this.info;
        }
        public void setLeft(IAVLNode node)
        {
            this.nodeL=node;
            return;
        }
        public IAVLNode getLeft()
        {
            return this.nodeL;

        }
        public void setRight(IAVLNode node)
        {
            this.nodeR=node;
            return;
        }
        public IAVLNode getRight()
        {
            return this.nodeR;
        }
        public void setParent(IAVLNode node)
        {
            this.parent=node;
            return;
        }
        public IAVLNode getParent()
        {
            return this.parent;
        }
        public boolean isRealNode()
        {
            if (this.key==-1){return false;}
            return true;
        }
        public void setHeight(int height)
        {
            this.height=height;
            return;
            //this.height=REC_setheight(this);
            //return;
        }
        //public int REC_setheight(IAVLNode root)
        //{
        //   if(root==null){return 0;}
        //  if(!isRealNode()){return -1;}
        // int max = Math.max(root.getLeft().getHeight(),root.getRight().getHeight());
        //   return 1+ max;
        //}
        public int getHeight()
        {
            return this.height;
            //if (this!=null && isRealNode()){return height;}return -1;
        }
        public void setsize(int sl, int sr) {
            this.sizeleft = sl;
            this.sizeright = sr;
            this.size = sizeleft + sizeright + 1;
        }
        public int getsize()
        {
            return this.size;
        }
        public int getsizeleft()
        {
            return this.sizeleft;
        }
        public int getsizeright()
        {
            return this.sizeright;
        }
    }

}

