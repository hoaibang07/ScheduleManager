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
using Newtonsoft.Json;

namespace Client
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private async Task<string> WCFRESTServiceCall(string methodRequestType, string methodName, object bodyParam)
        {
            JsonSerializerSettings microsoftDateFormatSettings = new JsonSerializerSettings
            {
                DateFormatHandling = DateFormatHandling.MicrosoftDateFormat
            };
            string ServiceURI = "http://nckh.somee.com/Server.svc/rest/" + methodName;
            HttpClient httpClient = new HttpClient();
            HttpRequestMessage request = new HttpRequestMessage(methodRequestType == "GET" ? HttpMethod.Get : HttpMethod.Post, ServiceURI);
            if (bodyParam!=null)
            {
                request.Content = new StringContent(JsonConvert.SerializeObject(bodyParam,microsoftDateFormatSettings), Encoding.UTF8, "application/json");
            }
            HttpResponseMessage response = await httpClient.SendAsync(request);
            string returnString = await response.Content.ReadAsStringAsync();
            return returnString;
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            txtMethod.Text = "UpSync";
        }

        private async void btnClick_Click(object sender, EventArgs e)
        {
            JsonSerializerSettings microsoftDateFormatSettings = new JsonSerializerSettings
            {
                DateFormatHandling = DateFormatHandling.MicrosoftDateFormat
            };
            txtResult.Text = "";
            Task t = new Task() { ID ="0100",AccountName="nguyendung", BeginTime = new DateTime(2015,1,1), EndTime = new DateTime(2015, 12, 12), Place = "DHH", TaskContent = "Coi thi", TaskName = "Lich coi thi" , Type = 1};
            DuLieu dt = new DuLieu() { password = "nguyendung", userName = "nguyendung", task = t};
            //MessageBox.Show(JsonConvert.SerializeObject(dt, microsoftDateFormatSettings));
            txtResult.Text = await WCFRESTServiceCall("POST", txtMethod.Text, dt);
        }
    }
}
