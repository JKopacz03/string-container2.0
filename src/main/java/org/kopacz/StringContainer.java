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

    public StringContainer() {
    }

    public StringContainer(String pattern, Boolean duplicatedNotAllowed) {
        this.pattern = pattern;
        this.duplicatedNotAllowed = duplicatedNotAllowed;
    }

    private StringContainer(Node head, String pattern, Boolean duplicatedNotAllowed, int size) {
        this.head = head;
        this.pattern = pattern;
        this.duplicatedNotAllowed = duplicatedNotAllowed;
        this.size = size;
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

    public static class Node implements Serializable{
        private Node next;
        private String data;
        private LocalDateTime addTime = LocalDateTime.now();

        private Node() {
        }

        private Node(Node next, String data, LocalDateTime addTime) {
            this.next = next;
            this.data = data;
            this.addTime = addTime;
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

    private void add(Node an) {
        Node node = new Node(null, an.data, an.addTime);
        if (head == null) {
            head = node;
        } else {
            Node n = head;
            while (n.next != null) {
                n = n.next;
            }
            n.next = node;
        }
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

    public void show() {
        Node n = head;
        while (n.next != null) {
            System.out.println(n);
            n = n.next;
        }
        System.out.println(n);
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

    public Node get(String object) {
        Node n = head;
        while (!Objects.equals(n.data, object)) {
            n = n.next;
        }
        return n;
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
                stringContainer.add(n);
            }
            n = n.next;
        }
        return stringContainer;
    }

    private static boolean validateDateFrom(LocalDateTime date, Node n) {
        if(Objects.isNull(date)){
            return true;
        }
        return n.addTime.isAfter(date) || n.addTime.equals(date);
    }

    private static boolean validateDateTo(LocalDateTime date, Node n) {
        if(Objects.isNull(date)){
            return true;
        }
        return n.addTime.isBefore(date) || n.addTime.equals(date);
    }

    public void storeToFile(String name){

        StringContainer stringContainer = new StringContainer(head, pattern, duplicatedNotAllowed, size);


        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(name))) {
            outputStream.writeObject(stringContainer);
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

