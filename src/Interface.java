import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class Interface extends JFrame {
        public String[] difficulty = {"Easy", "Normal", "Hard"};
        public Integer difficulty_int = 0; // 0 - easy, 1 - normal, 2 - hard
        public void startScreen(){
            //Tworzenie okna programu
            JFrame start_screen = new JFrame("Sudoku");
            start_screen.setSize(700,700);
            start_screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            start_screen.setLocationRelativeTo(null);

            //Przycisk New Game
            JButton new_game = new JButton("New Game");
            new_game.setBounds(250,50,200,100);
            start_screen.add(new_game);

            //Przycisk Load Game
            JButton load_game = new JButton("Load Game");
            load_game.setBounds(250,200,200,100);
            start_screen.add(load_game);

            //Przycisk Records
            JButton records = new JButton("Records");
            records.setBounds(250,350,200,100);
            start_screen.add(records);

            //Przycisk Exit
            JButton exit = new JButton("Exit");
            exit.setBounds(250,500,200,100);
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
                    set_difficulty.setSize(400,200);
                    set_difficulty.setLocationRelativeTo(null);
                    set_difficulty.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    //ComboBox z wyborem poziomu
                    JComboBox choose_difficulty = new JComboBox(difficulty);
                    choose_difficulty.setBounds(100,25,200,50);
                    choose_difficulty.setSelectedIndex(0);
                    set_difficulty.add(choose_difficulty);

                    //Przycisk OK potwierdzający wybór
                    JButton ok = new JButton("OK");
                    ok.setBounds(75,90,250,50);
                    set_difficulty.add(ok);

                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            difficulty_int = choose_difficulty.getSelectedIndex();
                            set_difficulty.dispose();
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
                    load_screen.setSize(300,500);
                    load_screen.setLocationRelativeTo(null);
                    load_screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    JButton slot_1 = new JButton("Slot 1");
                    slot_1.setBounds(75,50,150,50);
                    load_screen.add(slot_1);

                    JButton slot_2 = new JButton("Slot 2");
                    slot_2.setBounds(75,150,150,50);
                    load_screen.add(slot_2);

                    JButton slot_3 = new JButton("Slot 3");
                    slot_3.setBounds(75,250,150,50);
                    load_screen.add(slot_3);

                    JButton slot_4 = new JButton("Slot 4");
                    slot_4.setBounds(75,350,150,50);
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
                    records_list.setSize(600,600);
                    records_list.setLocationRelativeTo(null);
                    records_list.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    records_list.setLayout(null);
                    records_list.setVisible(true);
                }
            });

            start_screen.setLayout(null);
            start_screen.setVisible(true);

        }

        public static void main(String[] args){
            Interface gra = new Interface();
            gra.startScreen();
        }
    }


