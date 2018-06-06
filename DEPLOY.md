# /usr/lib/systemd/system/file-manager.service
```
[Unit]
Description=file-manager
After=network.target
#After=network.target remote-fs.target nss-lookup.target

[Service]
#no funciona Enviroment
#Enviroment=interlis_uploadedfiles=/tmp/uploads
#Enviroment=interlis_ilidir=/tmp/ili
ExecStart=/bin/bash -c "java -jar /opt/ilivalidator/file-manager-0.1.0.jar"
Type=simple
#ExecStop=/usr/lib/systemd/scripts/apachectl stop
#RemainAfterExit=yes
User=file-manager
Group=file-manager

[Install]
WantedBy=default.target
```

# /etc/sudoers
```
file-manager ALL=(ALL) NOPASSWD: /bin/systemctl start file-manager
file-manager ALL=(ALL) NOPASSWD: /bin/systemctl restart file-manager
file-manager ALL=(ALL) NOPASSWD: /bin/systemctl stop file-manager
file-manager ALL=(ALL) NOPASSWD: /bin/systemctl status file-manager
file-manager ALL=(ALL) NOPASSWD: /bin/systemctl status file-manager -l
```
