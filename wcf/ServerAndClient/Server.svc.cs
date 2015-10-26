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
            else
                return false;
        }


        public List<Task> DownSync(string userName)
        {
            TaskMDBEntities db = new TaskMDBEntities();
            var query = from t in db.Tasks
                        where t.AccountName == userName
                        select t;
            List<Task> ts = query.ToList<Task>();
            return ts;
        }

        public bool UpSync(string userName, Task t)
        {
            try
            {
                TaskMDBEntities db = new TaskMDBEntities();
                Task t0 = new Task();
                t0 = t;
                db.Tasks.Add(t0);
                db.SaveChanges();
                return true;
            }
            catch { return false; }
        }
    }
}
