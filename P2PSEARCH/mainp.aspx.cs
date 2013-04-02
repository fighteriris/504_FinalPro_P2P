using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using MySql;
using MySql.Data.MySqlClient;

public partial class mainp : System.Web.UI.Page
{
    private MySqlConnection connection;
    private string server;
    private string database;
    private string uid;
    private string password;
    protected void Page_Load(object sender, EventArgs e)
    {
        Initialize();
    }
    private void Initialize()
    {
        server = "localhost";
        database = "p2psearch_webpage_test";
        uid = "root";
        password = "000000";
        string connectionString;
        connectionString = "SERVER=" + server + ";" + "DATABASE=" +
        database + ";" + "UID=" + uid + ";" + "PASSWORD=" + password + ";";

        connection = new MySqlConnection(connectionString);
    }

    protected void search_b_Click(object sender, EventArgs e)
    {
        try
        {
            connection.Open();
            output.Text = "open sucess!";
            //return true;
            
    string query = "SELECT * FROM webpage";

    //Create a list to store the result
    List< string >[] list = new List< string >[2];
    list[0] = new List< string >();
    list[1] = new List< string >();
   

    //Open connection
   
        //Create Command
        MySqlCommand cmd = new MySqlCommand(query, connection);
        //Create a data reader and Execute the command
        MySqlDataReader dataReader = cmd.ExecuteReader();
        
        //Read the data and store them in the list
        while (dataReader.Read())
        {
            list[0].Add(dataReader["PAGE_URL"] + "");
            list[1].Add(dataReader["PAGE_Content"] + "");
            // list[2].Add(dataReader["PAGE_RANK"] + "");
            ListItem f = new ListItem(dataReader["PAGE_URL"] + "" + dataReader["PAGE_Content"] + "");
            BulletedList1.Items.Add(f);
        }
        //close Data Reader
        dataReader.Close();

        //close Connection
 
}
       
        catch (MySqlException ex)
        {
            //When handling errors, you can your application's response based 
            //on the error number.
            //The two most common error numbers when connecting are as follows:
            //0: Cannot connect to server.
            //1045: Invalid user name and/or password.
           
        }
    }
}