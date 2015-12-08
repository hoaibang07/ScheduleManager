using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace ServerAndClient
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Server" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Server.svc or Server.svc.cs at the Solution Explorer and start debugging.
    public class Server : IServer
    {
        public void DoWork()
        {

        }

        public bool Login(string usr, string pwd)
        {
            TaskMDBEntities db = new TaskMDBEntities();
            var query = from x in db.Accounts
                        where x.AccountName == usr && x.Password == pwd
                        select x.AccountID;
            if (query.Count() == 1)
                return true;
            return false;
        }


        public List<Task> DownSync(string usr, string dis)
        {
            int distance = int.Parse(dis);
            DateTime temp = DateTime.Now;
            DateTime now = new DateTime(temp.Year, temp.Month, temp.Day);
            DateTime begin, end;
            if (distance>=0)
            {
                begin = now;
                end = begin.AddDays(distance);

            }
            else
            {
                end = now;
                begin = end.AddDays(distance);
            }
            TaskMDBEntities db = new TaskMDBEntities();
            var query = from t in db.Tasks
                        where t.AccountName == usr && t.Type == 1 && t.BeginTime >= begin && t.EndTime <= end
                        select t;
            List<Task> ts = query.ToList<Task>();
            return ts;
        }

        public string UpSync(DuLieu data)
        {
            string usr = data.userName;
            string pwd = data.password;
            Task task = new Task();
            //task = Newtonsoft.Json.JsonConvert.DeserializeObject<Task>(tsk);
            task = data.task;
            if (Login(usr, pwd))
            {
                try
                {
                    TaskMDBEntities db = new TaskMDBEntities();
                    string id = task.ID;
                    var query = from x in db.Tasks
                                where x.ID == id
                                select x;
                    if (query.ToList().Count==0)
                    {
                        // Neu khong ton tai
                        Task t0 = new Task();
                        t0 = task;
                        db.Tasks.Add(t0);
                        db.SaveChanges();
                        return "OK - Da them";
                    }
                    else
                    {
                        // Neu ton tai
                        Task c = (from x in db.Tasks
                                  where x.ID == id
                                  select x).FirstOrDefault();
                        c.Place = task.Place;
                        c.TaskContent = task.TaskContent;
                        c.TaskName = task.TaskName;
                        c.BeginTime = task.BeginTime;
                        c.EndTime = task.EndTime;
                        c.Type = task.Type;
                        db.SaveChanges();
                        return "OK - Da sua";
                    }
                    
                }
                catch (Exception ex) { return ex.Message; }
            }
            return "Dang nhap that bai. Chi tiet: Username=" + usr + ", Password=" + pwd;
        }
    }
}
