using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Net.Http;
namespace Client
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }
        //private async void btnGetMessage_Click(object sender, RoutedEventArgs e)
        //{
        //    string result = await WCFRESTServiceCall("GET", "GetMessage");
        //    var dialog = new MessageDialog(result);
        //    await dialog.ShowAsync();
        //}

        //private async void btnPostMessage_Click(object sender, RoutedEventArgs e)
        //{
        //    string result = await WCFRESTServiceCall("POST", "PostMessage", "\"Nguyen Thanh Tung\"");
        //    var dialog = new MessageDialog(result);
        //    await dialog.ShowAsync();
        //}

        ///// <summary>
        ///// Utility function to get/post WCFRESTService
        ///// </summary>
        ///// <param name="methodRequestType">RequestType:GET/POST</param>
        ///// <param name="methodName">WCFREST Method Name To GET/POST</param>
        ///// <param name="bodyParam">Parameter of POST Method (Need serialize to JSON before passed in)</param>
        ///// <returns>Created by tungnt.net - 1/2015</returns>
        //private async Task<string> WCFRESTServiceCall(string methodRequestType, string methodName, string bodyParam = "")
        //{
        //    string ServiceURI = "http://localhost:8080/server.svc" + methodName;
        //    HttpClient httpClient = new HttpClient();
        //    HttpRequestMessage request = new HttpRequestMessage(methodRequestType == "GET" ? HttpMethod.Get : HttpMethod.Post, ServiceURI);
        //    if (!string.IsNullOrEmpty(bodyParam))
        //    {
        //        request.Content = new StringContent(bodyParam, Encoding.UTF8, "application/json");
        //    }
        //    HttpResponseMessage response = await httpClient.SendAsync(request);
        //    string returnString = await response.Content.ReadAsStringAsync();
        //    return returnString;
        //}
    }
}
