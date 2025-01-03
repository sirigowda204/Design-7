// Time complexity = O(1)
// Space complexity = O(n)

class LFUCache {

  class Node {
    int key;
    int value;
    int freq;
    Node next;
    Node prev;
    public Node (int key, int value) {
      this.key = key;
      this.value = value;
      this.freq = 1;
    }
  }

  class DLList {
    Node head;
    Node tail;
    int size;
    public DLList() {
      this.head = new Node(-1,-1);
      this.tail = new Node(-1,-1);
      this.head.next = this.tail;
      this.tail.prev = this.head;
      size = 0;
    }

    private void addToHead(Node node) {
      node.next = head.next;
      head.next = node;
      node.next.prev = node;
      node.prev = head;
      size++;
    }

    private void removeNode(Node node) {
      if (node == null || node.prev == null || node.next == null) return;
      node.prev.next = node.next;
      node.next.prev = node.prev;
      node.next = null;
      node.prev = null;
      size--;
    }

    private Node removeLastNode() {
      Node lastNode = tail.prev;
      removeNode(lastNode);
      return lastNode;
    }
  }

  Map<Integer, Node> map;
  Map<Integer, DLList> freqMap;
  int capacity;
  int min;

  public LFUCache(int capacity) {
    map = new HashMap<>();
    freqMap = new HashMap<>();
    this.capacity = capacity;
    this.min = 0;
  }

  public int get(int key) {
    if(map.containsKey(key)) {
      Node getNode = map.get(key);
      update(getNode);
      return getNode.value;
    }
    return -1;
  }

  public void put(int key, int value) {
    if(map.containsKey(key)) {
      Node node = map.get(key);
      update(node);
      node.value = value;
      return;
    }
    if(capacity == 0) return;
    if(capacity == map.size()) {
      DLList old = freqMap.get(min);
      Node removed = old.removeLastNode();
      map.remove(removed.key);
    }
    Node newNode = new Node(key, value);
    map.put(key, newNode);
    min = 1;
    DLList newList = freqMap.getOrDefault(1, new DLList());
    newList.addToHead(newNode);
    freqMap.put(1, newList);
  }

  private void update(Node node) {
    DLList oldList = freqMap.get(node.freq);
    oldList.removeNode(node);
    if(node.freq == min && oldList.size == 0) min++;
    node.freq++;
    DLList newList = freqMap.getOrDefault(node.freq, new DLList());
    newList.addToHead(node);
    freqMap.put(node.freq, newList);
  }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */