package org.mis.gen;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Questa classe provvede a leggere un seme da un file di testo chiamato
 * seme.txt Il seme verrà passato a tutti i metodi delle classi che ne faranno
 * richiesta.
 * @author Daniele Battista
 * @author Luca Dell'Anna
 * @author Enrico Orsini
 */
public class Seme {
	private static BufferedReader reader;
	private static boolean seme = true;
	private static int s;

	/**
	 * Metodo che restituisce un seme, il primo viene letto dal file, gli altri
	 * vengono generati incrementando il primo e verificando che non sia
	 * divisibile per 5
	 * 
	 * @return seme
	 */
	public static int getSeme() {
		if (seme) {
			String InputString = null;
			try {
				InputString = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			seme = false;
			return (s = Integer.parseInt(InputString));
		} else {
			if ((s + 2) % 5 == 0)
				return (s = s + 4);
			else
				return (s = s + 2);
		}
	}

	/**
	 * Metodo usato per aprire il file contenente il primo seme
	 */
	public static void apri() {
		File f1 = new File("seme.txt");
		reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new DataInputStream(new FileInputStream(f1))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		seme = true;
	}

	/**
	 * Metodo usato per chiudere il file contenente il primo seme
	 */
	public static void chiudi() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}