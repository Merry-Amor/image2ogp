import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        // メインフレームの作成
        JFrame frame = new JFrame("image2ogp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // テキストエリアの設定
        JTextArea mainTextArea = new JTextArea();
        // テキストエリアの高さと幅の設定
        mainTextArea.setPreferredSize(new Dimension(200, 100));

        // テキストの折り返しと単語単位の折り返しを有効化
        mainTextArea.setLineWrap(true);
        mainTextArea.setWrapStyleWord(true);

        // 画像選択ボタンを設置
        JButton selectButton = new JButton("画像を選択");
        selectButton.setBounds(30, 30, 120, 30);

        // ファイルパスを表示する場所を表示
        JLabel pathLabel = new JLabel("ファイルパスがここに表示されます");
        pathLabel.setBounds(30, 70, 340, 20);

        // OGP生成ボタンを設置
        JButton generateButton = new JButton("OGP生成");
        generateButton.setBounds(30, 30, 120, 30);

        // ステータスを表示する場所を表示
        JLabel statusLabel = new JLabel("ステータス：待機中");
        statusLabel.setBounds(30, 70, 340, 20);

        // レイアウトマネージャーの設定
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // テキストエリアの位置調整と配置およびスクロール設定の有効化
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        frame.add(new JScrollPane(mainTextArea), gbc);

        //画像選択ボタンを設置
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        frame.add(selectButton, gbc);

        // ファイルパス表示ラベルを設置
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        frame.add(pathLabel, gbc);

        // OGP変換ボタンを設置
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        frame.add(generateButton, gbc);

        // OGP変換ボタンを設置
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        frame.add(generateButton, gbc);

        // ステータスを表示する場所を設置
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        frame.add(statusLabel, gbc);

        // 画像選択ボタンがクリックされたとき
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                // 画像のみを選択できるようにフィルタを追加
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "画像ファイル（jpg, png, gif）", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    pathLabel.setText(filePath);
                }
            }
        });

        // OGP生成ボタンが押された時
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("ステータス：生成中");
                int imageWidth = makeCampas(pathLabel.getText());
                makeOgp("intermediateDeliverables.png", imageWidth, mainTextArea.getText());
                statusLabel.setText("ステータス：生成完了");
            }
        });

        frame.setVisible(true);
    }

    public static int makeCampas(String imageName) {
        int canvasWidth = 800;
        int canvasHeight = 419;

        try {
            // リサイズする

            BufferedImage original = ImageIO.read(new File(imageName));
            BufferedImage resized = ImageResizer.resizeByHeight(original, 419);
            ImageIO.write(resized, "png", new File("resized.png"));
            System.out.println("画像のリサイズが完了しました");
        } catch (IOException e) {
            System.out.println("画像のリサイズに失敗しました: " + e.getMessage());
        }

        try {

            // input.png を読み込む
            BufferedImage inputImage = ImageIO.read(new File("resized.png"));

            // 空のキャンバスを作成（白背景）
            BufferedImage canvas = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = canvas.createGraphics();

            // 背景を白に塗る
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, canvasWidth, canvasHeight);

            // input.png のサイズを取得
            int inputWidth = inputImage.getWidth();

            // input.png をキャンバスの左端に配置
            int x = 0;
            int y = 0;  // 中央揃え（垂直）

            g2d.drawImage(inputImage, x, y, null);
            g2d.dispose();

            // 出力ファイルに保存
            ImageIO.write(canvas, "png", new File("intermediateDeliverables.png"));
            System.out.println("画像の合成と保存が完了しました。");
            return inputWidth;

        } catch (IOException e) {
            System.out.println("画像の合成と保存に失敗しました: " + e.getMessage());
        }

        return 0;
    }

    public static void makeOgp(String imageName, int imageWidth, String mainText) {
        try {
            // 画像を読み込む
            BufferedImage image = ImageIO.read(new File(imageName)); // 入力画像パス

            // Graphics2D を取得
            Graphics2D g2d = image.createGraphics();

            // アンチエイリアスを有効にする（文字が滑らかに）
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // フォントと色を設定
            g2d.setFont(new Font("メイリオ", Font.PLAIN, 18));
            g2d.setColor(Color.BLACK);

            // 改行の処理
            String[] stringSplit = mainText.split("\n");

            int line = 0;

            for (int i = 0; i < stringSplit.length; i++) {

                line += 1;

                String[] stinrgSplit2 = stringSplit[i].split("");

                int stringSpot = 0;

                for (int j = 0; j < stinrgSplit2.length; j++) {
                    if ((imageWidth + (18 * stringSpot)) >= 800) {
                        line += 1;
                        stringSpot = 0;
                    }
                    System.out.println("stinrgSplit2[j] : " + stinrgSplit2[j] + " line : " + line);
                    g2d.drawString(stinrgSplit2[j], imageWidth + (18 * stringSpot), (18 * line)); // (x, y) は画像内の位置
                    stringSpot += 1;
                }
                if ((18 * line) > 419) {
                    break;
                }

            }

            // リソースを解放
            g2d.dispose();

            // 保存先ファイル名
            File output = new File("output.png");

            // 保存（拡張子に応じてJPEG, PNGなど）
            ImageIO.write(image, "png", output);

            System.out.println("画像にテキストを追加して保存しました。");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}