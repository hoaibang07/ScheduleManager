USE [master]
GO
/****** Object:  Database [TaskDB]    Script Date: 16/10/2015 2:12:11 CH ******/
CREATE DATABASE [TaskDB]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'TaskDB', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\TaskDB.mdf' , SIZE = 3072KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'TaskDB_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\TaskDB_log.ldf' , SIZE = 1024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [TaskDB] SET COMPATIBILITY_LEVEL = 100
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [TaskDB].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [TaskDB] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [TaskDB] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [TaskDB] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [TaskDB] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [TaskDB] SET ARITHABORT OFF 
GO
ALTER DATABASE [TaskDB] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [TaskDB] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [TaskDB] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [TaskDB] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [TaskDB] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [TaskDB] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [TaskDB] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [TaskDB] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [TaskDB] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [TaskDB] SET  DISABLE_BROKER 
GO
ALTER DATABASE [TaskDB] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [TaskDB] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [TaskDB] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [TaskDB] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [TaskDB] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [TaskDB] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [TaskDB] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [TaskDB] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [TaskDB] SET  MULTI_USER 
GO
ALTER DATABASE [TaskDB] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [TaskDB] SET DB_CHAINING OFF 
GO
ALTER DATABASE [TaskDB] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [TaskDB] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
ALTER DATABASE [TaskDB] SET DELAYED_DURABILITY = DISABLED 
GO
USE [TaskDB]
GO
/****** Object:  Table [dbo].[Account]    Script Date: 16/10/2015 2:12:11 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Account](
	[AccountID] [varchar](50) NOT NULL,
	[AccountName] [nvarchar](50) NOT NULL,
	[Password] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_Account] PRIMARY KEY CLUSTERED 
(
	[AccountID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK_Account] UNIQUE NONCLUSTERED 
(
	[AccountName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Task]    Script Date: 16/10/2015 2:12:11 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Task](
	[ID] [varchar](50) NOT NULL,
	[TaskName] [nvarchar](50) NOT NULL,
	[TaskContent] [nvarchar](max) NULL,
	[BeginTime] [smalldatetime] NOT NULL,
	[EndTime] [smalldatetime] NOT NULL,
	[Type] [int] NOT NULL,
	[AccountName] [nvarchar](50) NOT NULL,
	[Place] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_Task] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
USE [master]
GO
ALTER DATABASE [TaskDB] SET  READ_WRITE 
GO
