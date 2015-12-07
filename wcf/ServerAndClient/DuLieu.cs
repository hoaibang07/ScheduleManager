using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace ServerAndClient
{
    [DataContract]
    public class DuLieu
    {
        [DataMember]
        public String userName { get; set; }
        [DataMember]
        public String password { get; set; }
        [DataMember]
        public Task task { get; set; }
    }
}