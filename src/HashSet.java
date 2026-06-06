public class HashSet {
    private int size = 0;
    private final double loadFactor = 0.8;
    private Integer[] elementData = new Integer[16];
    private final int probeDistance = 1;
    private final Integer TOMBSTONE = Integer.MIN_VALUE;

    public HashSet() {
    }
    public HashSet(int initialCapacity) {
        if (initialCapacity > 0) {
            elementData = new Integer[initialCapacity];
        } else {
            throw new IllegalArgumentException("Initial capacity must be > 0");
        }
    }

    private int contains(int value) {
        int index = hashCode(value);

        return contains(value, index);
    }

    private int contains(int value, int index) {
        int counter = elementData.length;

        while (elementData[index] != null && counter > 0) {
            if (elementData[index] != TOMBSTONE && elementData[index].equals(value)) {
                return index;
            }
            index = (index + probeDistance) % elementData.length;
            counter--;
        }
        return -1;
    }

    private void resize() {
        Integer[] oldData = elementData;
        size = 0;
        elementData = new Integer[elementData.length * 2];

        for (Integer el : oldData) {
            if (el != null && el != TOMBSTONE) {
                add(el, false);
            }
        }
    }

    public boolean add(int toAdd) {
        return add(toAdd, true);
    }

    public boolean add(int toAdd, boolean shouldResize) {
        if (shouldResize) {
            if ((double) size / elementData.length >= loadFactor) {
                this.resize();
            }
        }
        int index = hashCode(toAdd);

        int counter = elementData.length;
        while (elementData[index] != null && counter > 0) {
            if (elementData[index] == TOMBSTONE) {
                if (contains(toAdd, index) >= 0) {
                    return false;
                } else {
                    break;
                }
            } else if (elementData[index].equals(toAdd)) {
                return false;
            }
            index = (index + probeDistance) % elementData.length;
            counter--;
        }
        elementData[index] = toAdd;
        size++;
        return true;
    }

    public boolean delete(int toDelete) {
        int hc = hashCode(toDelete);
        int index = contains(toDelete, hc);

        if (index == -1) {
            return false;
        } else {
            elementData[index] = TOMBSTONE;
            size--;
            return true;
        }
    }

    public Integer getValue(int value) {
        int index = contains(value);

        if (index >= 0) {
            return elementData[index];
        } else {
            return null;
        }
    }

    private int hashCode(int el) {
        if (el == Integer.MIN_VALUE) {
            el = Integer.MAX_VALUE;
        }
        return (Math.abs(el) * 13) % elementData.length;
    }

    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder output = new StringBuilder("[");

        if (elementData[0] == TOMBSTONE) {
            output.append("X");
        } else {
            output.append(elementData[0]);
        }
        for (int i = 1; i < elementData.length; i++) {
            if (elementData[i] != TOMBSTONE) {
                output.append(", ").append(elementData[i]);
            } else {
                output.append(", X");
            }
        }
        output.append("]");

        return output.toString();
    }
 }
