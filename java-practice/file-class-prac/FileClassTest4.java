import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class FileClassTest4
{
	public static void main(String[] arg)
	{
		if (arg.length != 1 || arg[0].length() != 1 || "tTILnN".indexOf(arg[0]) == -1) 
		{
			System.out.println("	SORT_OPTION : ");
			System.out.println("	t	Time ascending sort.");
			System.out.println("	T	Time descending sort");
			System.out.println("	l	Length ascending sort");
			System.out.println("	L	Length desceding sort");
			System.out.println("	n	Name asceding sort");
			System.out.println("	N	Name desceding sort");
			System.exit(0);
		}
		final char option = arg[0].charAt(0);

		String	curDir = System.getProperty("user.dir");
		File	dir = new File(curDir);
		File[]	files = dir.listFiles();

		Comparator	comp = new Comparator()
		{
			public int compare(Object o1, Object o2)
			{
				long time1 = ((File)o1).lastModified();
				long time2 = ((File)o2).lastModified();

				long length1 = ((File)o1).length();
				long length2 = ((File)o2).length();

				String name1 = ((File)o1).getName().toLowerCase();
				String name2 = ((File)o2).getName().toLowerCase();

				int result = 0;

				switch(option)
				{
					case 't' :
						if (time1 - time2 > 0)
							result = 1;
					case 'T' :
						if (time1 - time2 > 0)
							result= -1;
					case 'I' :
						if (length1 - length2 > 0)
							result = 1;
						else if (length1 - length2 == 0)
							result = 0;
						else if (length1 - length2 < 0)
							result = -1;
					case 'L' :
						if (length1 - length2 > 0)
							result = -1;
						else if (length1 - length2 == 0)
							result = 0;
						else if (length1 - length2 < 0)
							result = 1;
					case 'n' :
						result = name1.compareTo(name2);
						break ;
					case 'N' :
						result = name2.compareTo(name1);
						break ;
				}
				return result;
			}
			public boolean equals(Object o)
			{
				return false;
			}
		};

		Arrays.sort(files, comp);

		for (int i = 0; i < files.length; i++)
		{
			File f = files[i];
			String name = f.getName();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String attribute = "";
			String size = "";
			if (files[i].isDirectory())
			{
				attribute = "DIR";
			}
			else
			{
				size = f.length() + "";
				attribute = f.canRead() ? "R" : " ";
				attribute += f.canWrite() ? "W" : " ";
				attribute += f.isHidden() ? "H" : " ";
			}
			System.out.printf("%s %3s %6s %s%n", df.format(new Date(f.lastModified())), attribute, size, name);
		}
	}
}
