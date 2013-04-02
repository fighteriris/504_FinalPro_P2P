<%@ Page Language="C#" AutoEventWireup="true" CodeFile="mainp.aspx.cs" Inherits="mainp" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
    <asp:textbox id="search" runat="server" Height="18px" Width="385px" />
    <asp:button id="search_b" runat="server" Text="search" Width="138px" OnClick="search_b_Click" />
    </div>
        <asp:label ID="output"  runat="server"/>
        <div>
            
            </div>
        <asp:BulletedList ID="BulletedList1" runat="server"></asp:BulletedList>
    </form>
</body>
</html>
