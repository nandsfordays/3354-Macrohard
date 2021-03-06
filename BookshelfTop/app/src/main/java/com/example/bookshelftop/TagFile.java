/*
 * TagFile class: stores book tags in a file so they can be accessed later. If 2 books with the same name are imported they will share a
 * TagFile (old file will NOT be overwritten).
 *
 * @author Saurabh Shah
 *
 * @version 1
 *
 */

package com.example.docgrabber;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class TagFile {

    //string variables for holding book title and filepath for text file of book
    private String filePath = "";
    private String bookTitle = "";

    /**
     Constructor
     Creates a file in default directory (needs to be changed) and adds a
     title to the file along with 2 newlines

     * @param bookFile title of book that the TagFile is for
     */

    public TagFile(File bookFile)
    {
        bookTitle = bookFile.getName();
        filePath = ((bookFile.getPath()).substring(0,((bookFile.getPath()).length())-4))+"tags.txt";                //should be the filepath
        boolean fileDoesNotExist;

        try
        {
            File file = new File(filePath);
            fileDoesNotExist = file.createNewFile();
            if(fileDoesNotExist) {
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("Tags for " + bookTitle + ":");
                bw.newLine();
                bw.newLine();
                bw.write(">> ");
                bw.close();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    /*
    addTag function
    adds a tag to the tag file
    returns true is successful, returns false if not.
    >> signals a tag

     * @param newTag tag to be added
     * @return true if tag was added
     */

    public boolean addTag(String newTag)
    {
        try
        {
            File file = new File(filePath);
            file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(newTag);
            bw.newLine();
            bw.newLine();
            bw.write(">> ");
            bw.close();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

    }


    /*getTag method
     *
     * returns the tag of a book if it exists

     * @param checkTag checks if a tag is in the TagFile. Case matters
     * @return error message if no tags were found, the tag if the tag was found
     */
    public String getTag(String checkTag)
    {
        File file = new File(filePath);
        Scanner reader;
        try {
            reader = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return "No tags found";
        }

        while(reader.hasNextLine())
        {
            if(reader.next().equals(">>"))
            {
                String out =  (reader.nextLine()).substring(1);

                if(out.equals(checkTag))
                {
                    reader.close();
                    return out;
                }
            }
            else
            {
                reader.nextLine();
                continue;
            }
        }


        reader.close();
        return "No note found.";

    }



    /*deleteTag method
     *
     * removes a tag from the file
     */
    /**
     * @param badTag tag to be removed from file
     * @return true if tag was removed
     */

    public boolean deleteTag(String badTag)
    {
        //altpath is just filepath but the file name is "file2.txt instead of "file.txt""
        File original = new File(filePath);
        String altPath = filePath.substring(0,(filePath.length()-4)).concat("2"+filePath.substring((filePath.length()-4),filePath.length()));

        Scanner reader;
        try {
            reader = new Scanner(original);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try
        {
            File newFile = new File(altPath);
            newFile.createNewFile();
            FileWriter fw = new FileWriter(newFile);
            BufferedWriter bw = new BufferedWriter(fw);
            String nxtLn;

            bw.write(reader.nextLine());
            bw.newLine();
            bw.newLine();

            //creates alternate file with the name "file2.txt"
            while(reader.hasNextLine())
            {
                nxtLn = reader.nextLine();

                if(nxtLn.length() < 4)
                {
                    continue;
                }

                if((nxtLn.substring(0,2)).equals(">>"))
                {
                    String test =nxtLn.substring(3);

                    //if(Long.parseLong(nxtLn.substring(3,((nxtLn.substring(3)).indexOf(" ")))) == noteLoc)
                    if(test.equals(badTag))
                    {
                    }
                    else
                    {
                        bw.write(nxtLn);
                        bw.newLine();
                        bw.newLine();
                    }
                }

            }
            bw.write(">> ");
            bw.close();

            reader.close();

            original.delete();

            newFile.renameTo(new File(filePath));

            return true;

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }


        reader.close();

        return false;

    }



    /*
     * changeTag method
     *
     * calls the delete tag and add tag method to edit a tagFile
     */
    /**
     * @param newTag tag to be added
     * @param oldTag tag to be removed
     * @return true if operations completed successfully
     */

    public boolean changeTag(String newTag, String oldTag)
    {
        try
        {
            this.deleteTag(oldTag);
            this.addTag(newTag);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }



    /* listTags
     *
     * returns a list of tags
     *
     */
    /**
     * @return list of strings containing all tags for the book
     */

    public List<String> listTags()
    {
        List<String> tagList = new ArrayList<String>();

        File file = new File(filePath);
        Scanner reader;
        try {
            reader = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        while(reader.hasNextLine())
        {
            if(reader.next().equals(">>"))
            {
                tagList.add(reader.nextLine());
            }
            else
            {
                reader.nextLine();
                continue;
            }
        }


        reader.close();

        return tagList;
    }

}
