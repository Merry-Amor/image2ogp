import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        int imageWidth = makeCampas("input.png");
        makeOgp("intermediateDeliverables.png", imageWidth);
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

    public static void makeOgp(String imageName, int imageWidth) {
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

            // テキストを描画
            g2d.drawString("こんにちは、世界！", imageWidth, 18); // (x, y) は画像内の位置

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