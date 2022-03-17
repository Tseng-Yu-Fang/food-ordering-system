
package hw18_common;

public class cratdata {

        private String id;
        private String menuname;
        private String number;
        private String money;

        cratdata(String id, String name ,String number ,String money) {
            this.id = id;
            this.menuname = name;
            this.number=number;
            this.money=money;
        }



        public String getId() {
            return id;
        }

        public void setId(String ID) {
            this.id = ID;
        }
        
        public String getNumber() {
            return number;
        }
        
        public String getMoney() {
            return money;
        }

        public void setnumber(String number) {
            this.number = number;
        }

        public String getName() {
            return menuname;
        }

        public void setName(String nme) {
            this.menuname = nme;
        }

        @Override
        public String toString() {
            return "id: " + id + " - " + "name: " + menuname;
        }

    }
