
package hw18_common;

public class customerdata {

        private String id;
        private String name;
        private String account;
        private String password;
        private String address;
        private String phone;

        customerdata(String id, String name ,String account ,String password,String address,String phone) {
            this.id = id;
            this.name = name;
            this.account=account;
            this.password=password;
            this.address=address;
            this.phone=phone;
        }



        public String getId() {
            return id;
        }

        public String getAccount() {
            return account;
        }
        
        public String getPassword() {
            return password;
        }
        
        public String getAddress() {
            return address;
        }
        
        public String getPhone() {
            return phone;
        }

        public void setAccount(String number) {
            this.account = number;
        }

        public String getName() {
            return name;
        }



    }
