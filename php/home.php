<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>
<html xmlns='http://www.w3.org/1999/xhtml'>
<head>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />
<title>Task Manager</title>
</head>

<body>
<table width='100%' border='1'>

  <tr>
    <td width='81%' align='center'><h1>Task Manager</h1></td>
    <td width='19%' rowspan='2' align='center' valign='top'>
	<p>
    <form id='form2' name='form2' method='post' action='home.php'>
      <p>
        <label for='textfield2'>Tài khoản</label>
        <input type='text' name='txtAccount' id='textfield2' />
      </p>
      <p>
        <label for='textfield3'>Mật khẩu</label>
        <input type='password' name='txtPassword' id='textfield3' />
      </p>
      <p>
        <input type='submit' name='btnLogin' id='button' value='Đăng nhập' />
      </p>
      
    </form>
	</p>
    <p>
      <?php
	  if (isset($_POST['btnLogin']))
		{
			if (autithencation())
				echo "Xin chào, <b>" . $_POST['txtAccount'] . "</b>";
			else 
				echo "<br><u>Đăng nhập thất bại</u></br>";
		}
	   ?>
      </p>
    </td>
  </tr>
  <tr>
            <?php
	  if (isset($_POST['btnLogin']))
	  	{
			echo "<td height='491' valign='top'><table width='100%' border='1'>
      <tr>
        <th width='15%' scope='col'>Tên công việc</th>
        <th width='34%' scope='col'>Nội dung công việc</th>
        <th width='12%' scope='col'>TGBT</th>
        <th width='12%' scope='col'>TGKT</th>
        <th width='17%' scope='col'>Địa điểm</th>
        <th width='10%' scope='col'>Loại</th>
      </tr>";
			$conn = new PDO('sqlsrv:Server=TaskMDB.mssql.somee.com;Database=TaskMDB', 'nguyenthanhduc06_SQLLogin_1', 'dvy245u9mu');

			$stmt = $conn->prepare('SELECT TaskName, TaskContent, BeginTime, EndTime, Place, Type FROM Task WHERE AccountName = ?');
			
			$stmt->setFetchMode(PDO::FETCH_ASSOC);
			
			$stmt->bindParam(1, $temp);
			$temp = $_POST['txtAccount'];
			$stmt->execute();
		

			while($row = $stmt->fetch()) 
			{
				echo "<tr><td> $row[TaskName] </td><td> $row[TaskContent] </td><td> $row[BeginTime] </td><td> $row[EndTime] </td><td> $row[Place] </td><td>";
				if($row['Type']==1)
					echo "Nội bộ";
				else
					echo "Cá nhân";
				echo "</td></tr>";	
			}
			$conn =null;
			echo  "</table></td>";
		}
	   ?>  

  

  </tr>
</table>

<?php 

function autithencation()
{
	$usr = $_POST['txtAccount'];
	$pwd = $_POST['txtPassword'];
	$conn = new PDO('sqlsrv:Server=TaskMDB.mssql.somee.com;Database=TaskMDB', 'nguyenthanhduc06_SQLLogin_1', 'dvy245u9mu');
	//Tạo Prepared Statement
	$stmt = $conn->prepare('SELECT AccountID FROM Account WHERE AccountName = ? AND Password= ?');
	
	$stmt->setFetchMode(PDO::FETCH_ASSOC);
	
	$stmt->bindParam(1, $a_usr);
	$stmt->bindParam(2, $a_pwd);
	$a_usr = $usr;
	$a_pwd = $pwd;
	$stmt->execute();

	$r = 0;
	while($row = $stmt->fetch()) {
		$r = $r+1;
	}
	$conn =null;	
	if ($r>=1)
		return true; 
	return false;
}
?>
</body>
</html>