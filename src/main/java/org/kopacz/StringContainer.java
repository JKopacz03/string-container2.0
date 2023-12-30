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

    public static class Node implements Serializable{
        private Node next;
        private String date;
        private LocalDateTime addTime = LocalDateTime.now();

        private Node() {
        }

        private Node(Node next, String date, LocalDateTime addTime) {
            this.next = next;
            this.date = date;
            this.addTime = addTime;
        }

        public LocalDateTime getAddTime() {
            return addTime;
        }

        @Override
        public String toString() {
            return "Node{" +
                    ", date='" + date + '\'' +
                    ", addTime=" + addTime +
                    '}';
        }
    }

    public void add(String date) {

        checkIfDateIsDuplicated(date);

        Pattern actualPattern = Pattern.compile(this.pattern);
        Matcher matcher = actualPattern.matcher(date);

        if(!matcher.find()){
            throw new InvalidStringContainerValueException(date);
        }

        Node node = new Node();
        node.date = date;
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

    public void add(Node an) {
        Node node = new Node(null, an.date, an.addTime);
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

    private void checkIfDateIsDuplicated(String date) {
        if(duplicatedNotAllowed && !Objects.isNull(head)){
            Node n = head;
            while (n.date != date) {
                if(n.next == null){
                    break;
                }
                n = n.next;
            }
            if(n.date.equals(date)){
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
        if (object.equals(head.date)) {
            head = head.next;
        } else {
            Node n = head;
            Node n1 = null;
            while(n.next.date != object){
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
        return n.date;
    }

    public Node get(String object) {
        Node n = head;
        while (n.date != object) {
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
        return n.addTime.isAfter(date);
    }

    private static boolean validateDateTo(LocalDateTime date, Node n) {
        if(Objects.isNull(date)){
            return true;
        }
        return n.addTime.isBefore(date);
    }

    public void storeToFile(String name){

        try (PrintWriter writer = new PrintWriter(new FileWriter(name, true))) {

            for (int i = 0; i < size; i++) {
                writer.write(get(i) + "\n");
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

