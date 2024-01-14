package org.kopacz;

import org.kopacz.exception.DuplicatedElementOnListException;
import org.kopacz.exception.InvalidStringContainerValueException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringContainer implements Serializable {

    private Node head;
    private String pattern;
    private Boolean duplicatedNotAllowed = false;
    private int size = 0;
    public StringContainer(String pattern) {
        this.pattern = pattern;
    }

    public StringContainer(String pattern, Boolean duplicatedNotAllowed) {
        this.pattern = pattern;
        this.duplicatedNotAllowed = duplicatedNotAllowed;
    }

    @Override
    public String toString() {
        return "StringContainer{" +
                "head=" + head +
                ", pattern='" + pattern + '\'' +
                ", duplicatedNotAllowed=" + duplicatedNotAllowed +
                ", size=" + size +
                '}';
    }

    public static class Node implements Serializable {
        private Node next;
        private String data;
        private LocalDateTime addTime = LocalDateTime.now();

        private Node(Node next, String data, LocalDateTime addTime) {
            this.next = next;
            this.data = data;
            this.addTime = addTime;
        }

        public Node() {
        }

        public LocalDateTime getAddTime() {
            return addTime;
        }

        @Override
        public String toString() {
            return "Node{" +
                    ", data='" + data + '\'' +
                    ", addTime=" + addTime +
                    '}';
        }
    }

    public void add(String data) {

        checkIfDataIsDuplicated(data);

        Pattern actualPattern = Pattern.compile(this.pattern);
        Matcher matcher = actualPattern.matcher(data);

        if(!matcher.find()){
            throw new InvalidStringContainerValueException(data);
        }

        Node node = new Node();
        node.data = data;
        node.next = null;
        if (head == null) {
            head = node;
        } else {
            Node n = head;
            while (n.next != null) {
                n = n.next;
            }
            n.next = node;
        }

        size++;
    }

    private void checkIfDataIsDuplicated(String data) {
        if(duplicatedNotAllowed && !Objects.isNull(head)){
            Node n = head;
            while (!Objects.equals(n.data, data)) {
                if(n.next == null){
                    break;
                }
                n = n.next;
            }
            if(n.data.equals(data)){
                throw new DuplicatedElementOnListException();
            }
        }
    }

    public void remove(int index) {
        if (index == 0) {
            head = head.next;
        } else {
            Node n = head;
            Node n1 = null;
            for (int i = 0; i < index - 1; i++) {
                n = n.next;
            }
            n1 = n.next;
            n.next = n1.next;
        }
        size--;
    }

    public void remove(String object) {
        if (object.equals(head.data)) {
            head = head.next;
        } else {
            Node n = head;
            Node n1 = null;
            while(!Objects.equals(n.next.data, object)){
                n = n.next;
            }
            n1 = n.next;
            n.next = n1.next;
        }
        size--;
    }

    public String get(int index) {
        Node n = head;
        int i = 0;
        while (i != index) {
            n = n.next;
            i++;
        }
        return n.data;
    }

    public int size(){
        return size;
    }

    public StringContainer getDataBetween(LocalDateTime dateFrom, LocalDateTime dateTo){
        StringContainer stringContainer = new StringContainer(pattern);

        Node n = head;

        for (int i = 0; i < size; i++) {
            boolean after = validateDateFrom(dateFrom, n);
            boolean before = validateDateTo(dateTo, n);
            if(after && before){
                stringContainer.add(n.data);
            }
            n = n.next;
        }
        return stringContainer;
    }

    private static boolean validateDateFrom(LocalDateTime date, Node n) {
        if(Objects.isNull(date)){
            return true;
        }
        return n.addTime.isAfter(date) || n.addTime.isEqual(date);
    }

    private static boolean validateDateTo(LocalDateTime date, Node n) {
        if(Objects.isNull(date)){
            return true;
        }
        return n.addTime.isBefore(date) || n.addTime.isEqual(date);
    }

    public void storeToFile(String name){

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(name))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static StringContainer fromFile(String name) {

        StringContainer stringContainer = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(name))) {
            stringContainer = (StringContainer) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return stringContainer;

    }
}

