package com.example.SIChess.Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpeningFrame extends JFrame {
    private JRadioButton option1;
    private JRadioButton option2;
    private JRadioButton option3;

    private JRadioButton option4;
    private JRadioButton option5;
    private JButton saveButton;
    private String selectedOption = "Людина проти людини";
    private String selectedTime;
    private JTextField playerNameField;

    public OpeningFrame() {
        // Встановлюємо заголовок вікна
        setTitle("Вибір опції");

        // Встановлюємо розмір вікна
        setSize(400, 400);

        setLocationRelativeTo(null);

        // Завершуємо програму при закритті вікна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Створюємо панель для компонентів
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Додаємо текстове поле для вводу імені гравця
        JLabel nameLabel = new JLabel("Ім'я гравця:");
        playerNameField = new JTextField();
        playerNameField.setSize(new Dimension(20, 30));
        panel.add(nameLabel);
        panel.add(playerNameField);

        // Створюємо радіокнопки
        option1 = new JRadioButton("Людина проти людини");
        option2 = new JRadioButton("Людина проти комп'ютера");
        option3 = new JRadioButton("Комп'ютер проти комп'ютера");

        option1.setSelected(true);

        // Додаємо радіокнопки в групу
        ButtonGroup group = new ButtonGroup();
        group.add(option1);
        group.add(option2);
        group.add(option3);

        // Додаємо радіокнопки на панель
        panel.add(option1);
        panel.add(option2);
        panel.add(option3);

        option4 = new JRadioButton("10 хв");
        option5 = new JRadioButton("15 хв");
        option4.setSelected(true);

        ButtonGroup group2 = new ButtonGroup();
        group2.add(option4);
        group2.add(option5);

        // Додаємо нові радіокнопки для вибору часу партії
        panel.add(option4);
        panel.add(option5);

        // Створюємо кнопку збереження
        saveButton = new JButton("Зберегти");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Зберігаємо вибрану опцію
                if (option1.isSelected()) {
                    selectedOption = "Людина проти людини";
                } else if (option2.isSelected()) {
                    selectedOption = "Людина проти комп'ютера";
                } else if (option3.isSelected()) {
                    selectedOption = "Комп'ютер проти комп'ютера";
                } else {
                    selectedOption = "Нічого не вибрано";
                }

                if (option4.isSelected()) {
                    selectedTime = "10 хв";
                } else if (option5.isSelected()) {
                    selectedTime = "15 хв";
                } else {
                    selectedTime = "Нічого не вибрано";
                }


                // Виводимо вибрану опцію в консоль (або зберігаємо іншим способом)
                System.out.println("Ім'я гравця: " + playerNameField.getText());
                System.out.println("Вибрана опція: " + selectedOption);
                System.out.println("Час партії: " + selectedTime);


                // Закриваємо вікно
                dispose();
            }
        });

        // Додаємо кнопку на панель
        panel.add(saveButton);

        // Додаємо панель у вікно
        add(panel);

        // Робимо вікно видимим
        setVisible(true);
    }

    public int getNumberOfEngines(){
        int result = 0;
        if(selectedOption.equalsIgnoreCase("Людина проти людини")){
            result = 0;
        }
        if(selectedOption.equalsIgnoreCase("Людина проти комп'ютера")){
            result = 1;
        }
        if(selectedOption.equalsIgnoreCase("Комп'ютер проти комп'ютера")){
            result = 2;
        }
        return result;
    }
    public static void main(String[] args) {
        // Створюємо екземпляр вікна
        new OpeningFrame();
    }
}

