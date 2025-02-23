
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class ThreadExample {

    private static List<Integer> numbersList = new ArrayList<>();
    private static volatile String inputText = "";
    private static volatile boolean isInputTextUpdated = false; 
    private static volatile Random Random = new Random();
   

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Statistics Example"); //ข้อความบนหน้าต่าง
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);//ขนาดของหน้าต่าง
        frame.setLayout(new GridLayout(5, 2));//การจัดเรียงของหน้าต่าง

        JLabel inputLabel = new JLabel("Input number: ");
        JTextField inputField = new JTextField();

        JLabel averageLabel = new JLabel("Average: ");
        JTextField averageField = new JTextField();
        averageField.setEditable(false);

        JLabel medianLabel = new JLabel("Median: ");
        JTextField medianField = new JTextField();
        medianField.setEditable(false);

        JLabel summaryLabel = new JLabel("Summary: ");
        JTextField summaryField = new JTextField();
        summaryField.setEditable(false); 

        JLabel coutJLabel = new JLabel("Numbers List: ");
        JTextField coutJTextField = new JTextField();
        coutJTextField.setEditable(false);

        frame.add(inputLabel);
        frame.add(inputField); 
        frame.add(averageLabel);
        frame.add(averageField);
        frame.add(medianLabel);
        frame.add(medianField);
        frame.add(summaryLabel);
        frame.add(summaryField);
        frame.add(coutJLabel);
        frame.add(coutJTextField);

        Thread inputThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                    if (isInputTextUpdated) {
                        try {
                            int number = Integer.parseInt(inputText);
                            numbersList.add(number);             
                        } catch (NumberFormatException e) {
                            String message = "Invalid input. Please enter a valid number.";
                            SwingUtilities.invokeLater(() -> summaryField.setText(message));
                        }
                        isInputTextUpdated = false; 
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread averageThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
        
                    if (!numbersList.isEmpty()) {                       
                        int midIndex = numbersList.size() / 2;
                        double[] totalSum = {0};          

                        Thread frontThread = new Thread(() -> {
                            double frontSum = 0;
                            for (int i = 0; i < midIndex; i++) {
                                frontSum += numbersList.get(i);
                            }
                            synchronized (totalSum) {
                                totalSum[0] += frontSum;  
                            }
                        });

                        Thread backThread = new Thread(() -> {
                            double backSum = 0;
                            for (int i = midIndex; i < numbersList.size(); i++) {
                                backSum += numbersList.get(i);
                            }
                            synchronized (totalSum) {
                                totalSum[0] += backSum; 
                            }
                        });
                        
                        frontThread.start();
                        backThread.start();
                        frontThread.join();
                        backThread.join();
        
                     
                        double average = totalSum[0] / numbersList.size();
                        SwingUtilities.invokeLater(() -> averageField.setText(String.valueOf(average)));  
        
                    } else {
                        SwingUtilities.invokeLater(() -> averageField.setText("0"));  
                    }
        
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();  
                }
            }
        });
     
        Thread medianThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);  
                    if (!numbersList.isEmpty()) {
                        Collections.sort(numbersList); 
                        int size = numbersList.size();
                        double median;
                        if (size % 2 == 0) {  
                            median = (numbersList.get(size / 2 - 1) + numbersList.get(size / 2)) / 2.0; // แก้ไข
                           // median = numbersList.get(size / 2 - 1) ; // แก้ไข
                        } else {
                            median = numbersList.get(size / 2);
                        }
                        SwingUtilities.invokeLater(() -> medianField.setText(String.valueOf(median)));  
                    } else {
                        SwingUtilities.invokeLater(() -> medianField.setText("0"));  
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        Thread summaryThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000); 
                    int sum = 0;
                    for (int num : numbersList) {
                        sum += num;  
                    }
                    String summary = String.format("Sum: %d", sum);  
                    SwingUtilities.invokeLater(() -> summaryField.setText(summary));  
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread coutThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                    String listString = numbersList.toString();
                    SwingUtilities.invokeLater(() -> coutJTextField.setText(listString));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    String listString = numbersList.toString();
                    SwingUtilities.invokeLater(() -> coutJTextField.setText(listString));
                }
            }
        });
        
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    inputText = inputField.getText();
                    isInputTextUpdated = true;
                    inputField.setText("");
                }
            }
        });

        Thread มู = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                   
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        Thread ฮา = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(Random.nextInt(1000, 5000));
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
       
         
        
        ฮา.start();
        มู.start();
        inputThread.start();
        averageThread.start();
        medianThread.start();
        summaryThread.start();
        coutThread.start();
        frame.setVisible(true);
    }
}
