using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace ServerAndClient
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IServer" in both code and config file together.
    [ServiceContract]
    public interface IServer
    {
        [OperationContract]
        void DoWork();
        [OperationContract]
        [WebGet(RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json, UriTemplate="Login/{usr}/{pwd}", BodyStyle=WebMessageBodyStyle.Wrapped)]
        bool Login(String usr, String pwd);
        [WebGet(ResponseFormat = WebMessageFormat.Json, UriTemplate = "DownSync/{usr}", BodyStyle=WebMessageBodyStyle.Wrapped)]
        List<Task> DownSync(string usr);
        [WebInvoke(Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        bool UpSync(string userName, Task t);
    }
}
