package lanSimulation.internals;

import java.io.IOException;
import java.io.Writer;

import lanSimulation.Network;

public class Printer extends Node {

	public Printer(String name) {
		super(name);
	}

	public Printer(String name, Node nextNode) {
		super(name, nextNode);
	}

	public void printXMLOn(StringBuffer buf, Network network) {
		buf.append("<printer>");
		buf.append(name_);
		buf.append("</printer>");
	}

	public void printOn(StringBuffer buf, Network network) {
		buf.append("Printer ");
		buf.append(name_);
		buf.append(" [Printer]");
	}

	public boolean printDocument(Network network, Packet document, Writer report) {
		String author = "Unknown";
		String title = "Untitled";
		int startPos = 0, endPos = 0;
		try {
			if (document.message_.startsWith("!PS")) {
				startPos = document.message_.indexOf("author:");
				if (startPos >= 0) {
					endPos = document.message_.indexOf(".", startPos + 7);
					if (endPos < 0) {
						endPos = document.message_.length();
					}
					;
					author = document.message_.substring(startPos + 7, endPos);
				}
				;
				startPos = document.message_.indexOf("title:");
				if (startPos >= 0) {
					endPos = document.message_.indexOf(".", startPos + 6);
					if (endPos < 0) {
						endPos = document.message_.length();
					}
					;
					title = document.message_.substring(startPos + 6, endPos);
				}
				;
				network.firstNode_.printAccounting(report, author, title);
				report.write(">>> Postscript job delivered.\n\n");
				report.flush();
			} else {
				title = "ASCII DOCUMENT";
				if (document.message_.length() >= 16) {
					author = document.message_.substring(8, 16);
				}
				;
				network.firstNode_.printAccounting(report, author, title);
				report.write(">>> ASCII Print job delivered.\n\n");
				report.flush();
			}
			;
		} catch (IOException exc) {
			// just ignore
		}
		;
		return true;
	}

}
