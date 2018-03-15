package io.xpx.notary.app.process;

import java.io.IOException;

public class ProximaXDaemonRunner implements Runnable {
	private static String OS = System.getProperty("os.name").toLowerCase();

	@Override
	public void run() {

		try {
			String cmd = "proximax.exe daemon";
			if (isUnix() || isMac()) {
				cmd = "./proximax daemon &> proximax.out &";
			}
			// assumes that ipfs is already installed.
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	private static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	private static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

	private static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// try {
	// String cmd = "ipfs daemon";
	// if (isUnix() || isMac()) {
	// cmd = "nohup ipfs daemon &>ipfs.out &";
	// }
	// // assumes that ipfs is already installed.
	// Process p = Runtime.getRuntime().exec(cmd);
	// p.waitFor();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }).start();
	// return this;

}
