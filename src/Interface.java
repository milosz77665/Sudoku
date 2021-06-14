import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class Interface extends JFrame {
    public String[] difficulty = {"Easy", "Normal", "Hard"};
    public Integer difficulty_int = 0; // 0 - easy, 1 - normal, 2 - hard
    public String[] slots = {"Slot 1", "Slot 2", "Slot 3", "Slot 4"};
    public Integer slots_int = 0; // 0 - easy, 1 - normal, 2 - hard
    public int minuty = 0;
    public int sekundy = 0;
    public ArrayList<int[][]> list;
    public JTable sudoku;
    int q = 0;
    String nickname = "PRM2T";
    int points;
    public ArrayList<Integer> full_cells = new ArrayList<>();
    public ArrayList<Integer> empty_cells = new ArrayList<>();
    JFrame game;
    int[][] save_matrix = new int[9][9];
    JPanel panel;
    JLabel label;
    JLabel timer_text;
    JLabel timer_time;
    Timer t;
    Font font = new Font("Arial", Font.BOLD, 30);
    Font font2 = new Font("Arial", Font.BOLD, 25);
    JButton check_button, print_button, save_button;
    SimpleDateFormat df = new SimpleDateFormat("mm:ss");
    int timeLf = 0;
    public void startScreen() {
        //Tworzenie okna programu
        JFrame start_screen = new JFrame("Sudoku");
        start_screen.setSize(700, 700);
        start_screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        start_screen.setLocationRelativeTo(null);

        //Przycisk New Game
        JButton new_game = new JButton("New Game");
        new_game.setBounds(250, 50, 200, 100);
        start_screen.add(new_game);

        //Przycisk Load Game
        JButton load_game = new JButton("Load Game");
        load_game.setBounds(250, 200, 200, 100);
        start_screen.add(load_game);

        //Przycisk Records
        JButton records = new JButton("Records");
        records.setBounds(250, 350, 200, 100);
        start_screen.add(records);

        //Przycisk Exit
        JButton exit = new JButton("Exit");
        exit.setBounds(250, 500, 200, 100);
        start_screen.add(exit);

        //Funcka zamknięcia programu przyciskiem Exit
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //Nowa Gra - wybór poziomu trudności
        new_game.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Okno wyboru poziomu trudności
                JFrame set_difficulty = new JFrame("Choose the difficulty level");
                set_difficulty.setSize(400, 200);
                set_difficulty.setLocationRelativeTo(null);
                set_difficulty.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                //ComboBox z wyborem poziomu
                JComboBox choose_difficulty = new JComboBox(difficulty);
                choose_difficulty.setBounds(100, 25, 200, 50);
                choose_difficulty.setSelectedIndex(0);
                set_difficulty.add(choose_difficulty);

                //Przycisk OK potwierdzający wybór
                JButton ok = new JButton("OK");
                ok.setBounds(75, 90, 250, 50);
                set_difficulty.add(ok);

                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        difficulty_int = choose_difficulty.getSelectedIndex();
                        set_difficulty.dispose();

//                            Ustawienie nagłówka
                        if (choose_difficulty.getSelectedItem() == "Easy") {
                            game = new JFrame("Sudoku - Easy");
                            difficulty_int = 0;
                            timeLf=0;
                        } else if (choose_difficulty.getSelectedItem() == "Normal") {
                            game = new JFrame("Sudoku - Normal");
                            difficulty_int = 1;
                            timeLf=600000;
                        } else {
                            game = new JFrame("Sudoku - Hard");
                            difficulty_int = 2;
                            timeLf=10000;
//                            timeLf=300000;
                        }
//                            Generowanie Sudoku
                        generateSudoku();
//                            Blokowanie uzupełnionych pól
                        MakeNonEditable();
//                            Tworzenie sudoku
                        sudoku = new JTable(9, 9) {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                boolean non_editable = true;
                                for (int e = 0; e < full_cells.size(); e = e + 2) {
                                    non_editable = (non_editable && !(row == full_cells.get(e) && column == full_cells.get(e + 1)));
                                }
                                return non_editable;
                            }
                        };
//                          Liczenie czasu
                        t = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (difficulty_int == 0) {
                                    timeLf=timeLf+1000;
                                    timer_time.setText(df.format(timeLf));
                                }else if(difficulty_int == 1 && timeLf>0){
                                    timeLf=timeLf-1000;
                                    timer_time.setText(df.format(timeLf));
                                }else if (difficulty_int == 2&& timeLf>0){
                                    timeLf=timeLf-1000;
                                    timer_time.setText(df.format(timeLf));
                                }else{
                                    t.stop();
                                    JOptionPane.showMessageDialog(game,
                                            "Time has run out",
                                            "Game Over",
                                            JOptionPane.WARNING_MESSAGE);
                                    sudoku.setEnabled(false);
                                    check_button.setEnabled(false);
                                    save_button.setEnabled(false);
                                }
                            }
                        });
                        t.setRepeats(true);
                        t.setCoalesce(true);
                        t.start();

                        panel = new JPanel(new FlowLayout());
                        label = new JLabel();

                        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//                            Ustawienia wizualne

                        sudoku.setFont(font);
                        label.setFont(font2);
                        JTextField text_field = new JTextField();
                        text_field.setFont(font);
                        text_field.setHorizontalAlignment(JTextField.CENTER);
                        DefaultCellEditor customCellEditor = new DefaultCellEditor(text_field);
                        for (int i = 0; i < 9; i++) {
                            sudoku.getColumnModel().getColumn(i).setPreferredWidth(50);
                            sudoku.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                            sudoku.getColumnModel().getColumn(i).setCellEditor(customCellEditor);
                        }
                        sudoku.setRowHeight(50);
                        sudoku.setCellSelectionEnabled(false);
//                            Wpisywanie liczb do tabeli
                        AddSudoku(sudoku, list.get(0));
//                            Guzik do sprawdzenia odpowiedzi
                        check_button = new JButton();
                        check_button.setText("Solution");
                        check_button.addActionListener(this::Solve);
                        check_button.setActionCommand("answer");

//                            Guzik do zapisywania screenshota
                        print_button = new JButton();
                        print_button.setText("Save as PNG");
                        print_button.addActionListener(this::SaveAsPNG);

//                            Guzik do zapisywania gry
                        save_button = new JButton();
                        save_button.setText("Save Game");
                        save_button.addActionListener(this::SaveGame);

//                            Tekst licznika
                        timer_text = new JLabel("Time: ");
                        timer_text.setFont(font2);
                        timer_time= new JLabel(df.format(timeLf));
                        timer_time.setFont(font2);
//                            Ustawienia okna

                        game.setSize(470, 700);
                        game.setLocationRelativeTo(null);
                        game.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        panel.add(timer_text);
                        panel.add(timer_time);
                        panel.add(sudoku, BorderLayout.LINE_START);
                        panel.add(check_button, BorderLayout.AFTER_LINE_ENDS);
                        panel.add(print_button, BorderLayout.AFTER_LINE_ENDS);
                        panel.add(save_button, BorderLayout.AFTER_LINE_ENDS);
                        panel.add(label, BorderLayout.SOUTH);
                        game.add(panel);

                        game.setVisible(true);
//                          Listener okna
                        WindowListener listener = new WindowAdapter() {
                            public void windowClosing(WindowEvent evt) {
                                t.stop();
                                new_game.setEnabled(true);
                                load_game.setEnabled(true);
                            }
                        };
                        game.addWindowListener(listener);

                        WindowListener game_on = new WindowAdapter() {
                            public void windowOpened(WindowEvent evt) {
                                new_game.setEnabled(false);
                                load_game.setEnabled(false);
                            }
                        };
                        game.addWindowListener(game_on);

//                          Listener tabeli
                        sudoku.getModel().addTableModelListener(new TableModelListener() {
                            public void tableChanged(TableModelEvent e) {


                                try {
                                    String value_in_cell = (String) sudoku.getValueAt(e.getLastRow(), e.getColumn());
                                    if (value_in_cell.equals("")) {
                                        // dzięki temu nie wyrzuca błędu przy usuwaniu nieprawiłowej wartości
                                    } else if (Integer.parseInt(value_in_cell) > 9 || Integer.parseInt(value_in_cell) < 1) {
//                                        JOptionPane.showMessageDialog(game,
//                                                "Wpisuj wartości tylko z zakresu [1, 9]",
//                                                "Error",
//                                                JOptionPane.ERROR_MESSAGE);
                                        sudoku.setValueAt("", e.getLastRow(), e.getColumn());
                                    }
                                } catch (NumberFormatException err) {
//                                    JOptionPane.showMessageDialog(game,
//                                            "Wpisuj tylko liczby całkowite",
//                                            "Error",
//                                            JOptionPane.ERROR_MESSAGE);
//                                    System.out.println(err);
                                    sudoku.setValueAt("", e.getLastRow(), e.getColumn());
                                }
                            }
                        });
                    }

                    public void Solve(ActionEvent solve) {
                        String action = solve.getActionCommand();
                        if (action.equals("answer")) {
//                            sudoku.isCellEditable(9,9);
                            int count = 0;
                            try {

                                for (int e = 0; e < empty_cells.size(); e = e + 2) {

                                    sudoku.getValueAt(empty_cells.get(e), empty_cells.get(e + 1)).toString();

                                }
                                for (int e = 0; e < empty_cells.size(); e = e + 2) {

                                    String temp = sudoku.getValueAt(empty_cells.get(e), empty_cells.get(e + 1)).toString();
                                    Integer temp2 = Integer.valueOf(temp);
                                    if (temp2 != list.get(1)[empty_cells.get(e)][empty_cells.get(e + 1)]) {
                                        count++;
                                        label.setText("Sudoku wasn't solved correctly!!");
                                        sudoku.getModel().setValueAt("", empty_cells.get(e), empty_cells.get(e + 1));
                                    } else if (count == 0) {
                                        label.setText("Nice. Sudoku was solved correctly!!");
                                        t.stop();
                                        sudoku.setEnabled(false);
                                        check_button.setEnabled(false);
                                    }
                                }
                                if(count == 0 && difficulty_int != 0){
                                    nickname = JOptionPane.showInputDialog(game, "What's your nickname?","PRM2T"); // Pytanie o  imię, które pojawi się na liście rekordów
                                    if(difficulty_int == 1){
                                        points = timeLf;
                                    }
                                    else if(difficulty_int == 2){
                                        points = timeLf*5;
                                    }
                                }
                            } catch (NullPointerException npe) {
                                JOptionPane.showMessageDialog(game,
                                        "Please, fill all cells",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);

                            }
                            if (count > 0) {
                                t.stop();
                                DrawSudoku(list.get(1));
                                sudoku.setEnabled(false);
                                check_button.setEnabled(false);
                            }
                        }
                    }

                    public void SaveAsPNG(ActionEvent save_as_png) {
                        t.stop();
                        String action = save_as_png.getActionCommand();
                        if (action.equals("Save as PNG")) {
                            try {
                                getSaveSnapShot(sudoku, "sudoku.png");
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }

                    public void SaveGame(ActionEvent save_game) {
                        String action = save_game.getActionCommand();
                        if (action.equals("Save Game")) {

                            JDialog slots_to_choose = new JDialog(game, "Choose slot");
                            slots_to_choose.setSize(250, 250);
                            slots_to_choose.setVisible(true);

                            JComboBox choose_slot = new JComboBox(slots);
                            choose_slot.setBounds(25, 25, 150, 50);
                            choose_slot.setSelectedIndex(0);
                            slots_to_choose.add(choose_slot);

                            Save();
                            try {

                                BufferedWriter writer = new BufferedWriter(new FileWriter("sudoku.txt"));
                                writer.write(Arrays.deepToString(list.get(0)));
                                writer.write(Arrays.deepToString(list.get(1)));
                                writer.write(Arrays.deepToString(list.get(2)));

                                writer.close();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    }

                });

                set_difficulty.setLayout(null);
                set_difficulty.setVisible(true);
            }
        });

        //Load Game - wybór zapisu
        load_game.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame load_screen = new JFrame("Choose save file");
                load_screen.setSize(300, 500);
                load_screen.setLocationRelativeTo(null);
                load_screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JButton slot_1 = new JButton("Slot 1");
                slot_1.setBounds(75, 50, 150, 50);
                load_screen.add(slot_1);

                JButton slot_2 = new JButton("Slot 2");
                slot_2.setBounds(75, 150, 150, 50);
                load_screen.add(slot_2);

                JButton slot_3 = new JButton("Slot 3");
                slot_3.setBounds(75, 250, 150, 50);
                load_screen.add(slot_3);

                JButton slot_4 = new JButton("Slot 4");
                slot_4.setBounds(75, 350, 150, 50);
                load_screen.add(slot_4);

                load_screen.setLayout(null);
                load_screen.setVisible(true);
            }
        });

        //Records - tablica rekoródw
        records.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame records_list = new JFrame("List of records");
                records_list.setSize(600, 600);
                records_list.setLocationRelativeTo(null);
                records_list.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                records_list.setLayout(null);
                records_list.setVisible(true);
            }
        });

        start_screen.setLayout(null);
        start_screen.setVisible(true);

    }

    // cos tam do zapisu macierzy w liscie
    public void Save() {
        if (list.size() == 3) {
            list.remove(2);
        } else
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (sudoku.getModel().getValueAt(r, c) == null) {
                        System.out.println("xd");
                        save_matrix[r][c] = 0;
                    } else {
                        Integer x = Integer.parseInt(sudoku.getModel().getValueAt(r, c).toString());
                        System.out.println(x);
                        save_matrix[r][c] = Integer.parseInt(sudoku.getModel().getValueAt(r, c).toString());
                    }
                }
            }
        System.out.println(Arrays.deepToString(save_matrix));
        list.add(save_matrix);
    }

    // Zapisywanie screenshota
    public static BufferedImage getScreenShot(Component component) {

        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics());
        return image;
    }

    public static void getSaveSnapShot(Component component, String fileName) throws Exception {
        BufferedImage img = getScreenShot(component);
        ImageIO.write(img, "png", new File(fileName));
    }


    public void DrawSudoku(int[][] matrix) {
        JFrame solution = new JFrame();
        JTable table = new JTable(9, 9);
        JPanel panel2 = new JPanel();
        JLabel label2 = new JLabel("Solution");
        label2.setFont(font2);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//                            Ustawienia wizualne
        table.setFont(font);
        JTextField text_field = new JTextField();
        text_field.setFont(font);
        text_field.setHorizontalAlignment(JTextField.CENTER);
        DefaultCellEditor customCellEditor = new DefaultCellEditor(text_field);
        for (int i = 0; i < 9; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(50);
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(i).setCellEditor(customCellEditor);
        }
        table.setRowHeight(50);
        table.setCellSelectionEnabled(false);
//                            Wpisywanie liczb do tabeli
        AddSudoku(table, matrix);
//        Ustawienia okna
        solution.setSize(500, 550);
        solution.setLocationRelativeTo(null);
        solution.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        table.setEnabled(false);
        panel2.add(table);
        panel2.add(label2);
        solution.add(panel2);
        solution.setVisible(true);
    }

    //    public void changeFont(){
//        JTextField text_field1 = new JTextField();
//        Font font = new Font("Arial", Font.BOLD, 30);
//        text_field1.setFont(font);
//        text_field1.setForeground(Color.RED);
//        text_field1.setHorizontalAlignment(JTextField.CENTER);
//        DefaultCellEditor customCellEditor = new DefaultCellEditor(text_field1);
//
//    }
    public void generateSudoku() {
        if (q > 0) {
            list.clear();
        }
        SudokuGenerator generate_matrix = new SudokuGenerator();
        list = generate_matrix.SudokuLoop();
        printNums(list.get(1));
        q++;

    }

    public void MakeNonEditable() {
        if (q > 0) {
            full_cells.clear();
        }
        for (Integer i = 0; i < 9; i++) {
            for (Integer j = 0; j < 9; j++) {
                if (list.get(0)[i][j] != 0) {
                    full_cells.add(i);
                    full_cells.add(j);
                }
            }
        }
    }

    public void AddSudoku(JTable table, int[][] matrix) {
        if (q > 0) {
            empty_cells.clear();
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print("\t" + matrix[i][j]);
                if (matrix[i][j] != 0) {
                    table.getModel().setValueAt(matrix[i][j], i, j);
                } else {
                    empty_cells.add(i);
                    empty_cells.add(j);
                }

            }
            System.out.println();
        }
        System.out.println();
    }

    private void printNums(int[][] values) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print("\t" + values[i][j]);
            }
            System.out.println();
        }
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(Color.green);

        return this;
    }

    public static void main(String[] args) {
        Interface gra = new Interface();
        gra.startScreen();
    }
}
