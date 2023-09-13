import com.mpatric.mp3agic.*;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Scanner;
import javax.swing.*;

public class Practica {

    

    /*
     * Función que recibe como parámetro la ubicación del MP3
     */
     public static void mp3_fun(String cancion) throws Exception{
        Scanner sc = new Scanner(System.in);
        String camino;
        // Lee el MP3
        Mp3File mp3file = new Mp3File(cancion);
        System.out.println("Duración canción: " + mp3file.getLengthInSeconds() + " segundos");
        System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
        System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
        System.out.println("Tiene ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "SI" : "NO"));
        System.out.println("Tiene ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
        // Nos importa si tiene etiqueta DI3V2

        if (mp3file.hasId3v2Tag()) { //Si si tiene etiqueta ID3V2 entonces despliega los metadatos
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            id3v2Tag.setArtist("Mariano Pena");
            System.out.println("#Canción: " + id3v2Tag.getTrack());
            System.out.println("Artista: " + id3v2Tag.getArtist());
            System.out.println("Título: " + id3v2Tag.getTitle());
            System.out.println("Álbum: " + id3v2Tag.getAlbum());

            byte[] albumImageData = id3v2Tag.getAlbumImage();

            // Si tiene carátula guara la caratula como una imágen JPG
            if (albumImageData != null) {
                System.out.println("TU MP3 SI TIENE CARATULA\n\nDisfruta tu cancion!!!\n\n");
                System.out.println("Have album image data, length: " + albumImageData.length + " bytes");
                System.out.println("Album image mime type: " + id3v2Tag.getAlbumImageMimeType());

                BufferedImage img = ImageIO.read(new ByteArrayInputStream(albumImageData));
                File outputfile = new File("caratula.jpg");
                File outputfile2 = new File("caratula.bmp");
                ImageIO.write(img, "png", outputfile);
                ImageIO.write(img, "bmp", outputfile2);

                //Reproducimos el archivo de audio en caso de que si tenga la caratula
                String cmd = "cmd /c start "+cancion;
                Runtime.getRuntime().exec(cmd);

            } else { // Si no tiene carátula, le pone una nueva a la canción y la guarda como una nueva.
                System.out.println("TU MP3 NO TIENE CARATULA\n\n");

                //Se le pone una nueva caratula a la canción
                System.out.println("Selecciona la imagen que quieres ponerle a la canción");
                //Abrir el explorador de archivos y seleccionar el archivo con JFileChooser
                JFileChooser chooser = new JFileChooser();

                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, PNG, BMP & GIF Images", "jpg", "gif", "bmp", "png");

                int response;
                File archivo;

                chooser.setFileFilter(filter);

                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                response = chooser.showOpenDialog(null);

                if(response == JFileChooser.APPROVE_OPTION){
                    archivo = chooser.getSelectedFile();
                    System.out.println("El archivo seleccionado es: " + archivo.getAbsolutePath());

                    BufferedImage bImage = ImageIO.read(archivo);
                    //Obtenenmos la extension del archivo y la guardamos en una variable String para poder usarla despues
                    String extension = archivo.getAbsolutePath().substring(archivo.getAbsolutePath().lastIndexOf(".")+1);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(bImage, extension, bos );
                    byte [] data = bos.toByteArray();
                    id3v2Tag.setAlbumImage(data, "image/"+extension);
                    String nuevacancion = "D:\\Java_Projects\\Metadatos\\Metadatos\\musica\\nuevo_audio.mp3";
                    mp3file.save(nuevacancion);
                } else {
                    System.out.println("No se seleccionó ningún archivo");
                    archivo = null;
                }
                
               

                camino = sc.nextLine();

                /*BufferedImage bImage = ImageIO.read(new File("D:\\Descargas\\volcan.bmp"));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "bmp", bos );
                byte [] data = bos.toByteArray();
                id3v2Tag.setAlbumImage(data, "image/bmp");
                String nuevacancion = "D:\\Java_Projects\\Metadatos\\Metadatos\\musica\\nuevo_audio.mp3";
                mp3file.save(nuevacancion);*/
                
            }
        }
    }

    public static void main(String[] args) throws Exception{
        System.out.println("**********PRACTICA 01**********\n\n\n");
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3", "mp3");
        int response;
        File archivo;
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        response = chooser.showOpenDialog(null);

        if(response == JFileChooser.APPROVE_OPTION){
                    archivo = chooser.getSelectedFile();
                    System.out.println("El archivo seleccionado es: " + archivo.getAbsolutePath());
                    //Obtenenmos la extension del archivo y la guardamos en una variable String para poder usarla despues
                    mp3_fun(archivo.getAbsolutePath());
                    System.exit(0);
                    
                } else {
                    System.out.println("No se seleccionó ningún archivo, fin del programa");
                    archivo = null;
                    //Finaliza el programa
                    System.exit(0);
                }
        /*String cancion = "D:\\\\Java_Projects\\\\Metadatos\\\\Metadatos\\\\musica\\\\BohemianRhapsody.mp3"; //Ubicacion del MP3
        mp3_fun(cancion); // Se llama a la función que detalla el MP3*/
    }
}
