import java.nio.file.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import static java.nio.file.StandardOpenOption.*;
import java.util.Scanner;
import java.text.*;

public class CreateBankFile
{
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		Path filename = Paths.get("AccountRecords.txt");
		Path file = filename.toAbsolutePath();

		final int NUMBER_OF_RECORDS = 10000;

		final String ACCOUNT_NUMBER_FORMAT = "0000";
		final String NAME_FORMAT = "        ";
		final int NAME_LENGTH = NAME_FORMAT.length();
		final String BALANCE_FORMAT = "00000.00";
		final String delimiter = ",";

		String defaultRecord = ACCOUNT_NUMBER_FORMAT + delimiter + NAME_FORMAT + delimiter + BALANCE_FORMAT + System.getProperty("line.separator");
		final int RECORD_SIZE = defaultRecord.length();

		FileChannel fc = null;
		String acctString;
		int acct;
		String name;
		double balance;
		byte[] data;
		ByteBuffer buffer;
		final String QUIT = "QUIT";


		createEmptyFile(file, defaultRecord, NUMBER_OF_RECORDS);

		try
		{
			fc = (FileChannel)Files.newByteChannel(file, CREATE, WRITE);

			System.out.print("Enter account number or " + QUIT + " >> ");
			acctString = input.nextLine();

			while(!(acctString.equals(QUIT)))
			{
				acct = Integer.parseInt(acctString);

				System.out.print("Account holder name for account #" + acctString + " >> ");
				name = input.nextLine();
				StringBuilder sb = new StringBuilder(name);
				sb.setLength(NAME_LENGTH);
				name = sb.toString();

				System.out.print("Balance of account #" + acctString + " >> ");
				balance = input.nextDouble();
				input.nextLine();
				DecimalFormat df = new DecimalFormat(BALANCE_FORMAT);

				String s = acctString + delimiter + name + delimiter + df.format(balance) + System.getProperty("line.separator");
				data = s.getBytes();
				buffer = ByteBuffer.wrap(data);

				fc.position(acct * RECORD_SIZE);
				fc.write(buffer);

				System.out.print("\nEnter account number or " + QUIT + " >> ");
				acctString = input.nextLine();
			}

			fc.close();
		}
		catch(Exception e)
		{
			System.out.println("Error message: " + e);
		}

		input.close();
	}

	public static void createEmptyFile(Path file, String s, int lines)
	{
		try
		{
			OutputStream outputStr = new BufferedOutputStream(Files.newOutputStream(file, CREATE));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStr));

			for(int count = 0; count < lines; ++count)
				writer.write(s, 0, s.length());

			writer.close();
		}
		catch(Exception e)
		{
			System.out.println("Error message: " + e);
		}
	}
}
