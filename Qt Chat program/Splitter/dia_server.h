#ifndef DIA_SERVER_H
#define DIA_SERVER_H

#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

#include <stdlib.h>
#include <string.h>

#include <QDialog>
#include <QLabel>
#include <QLineEdit>

namespace Ui
{
    class dia_server;
}

class dia_server : public QDialog
{
    Q_OBJECT

public:
    explicit dia_server(QWidget *parent = 0);
    ~dia_server();
    QString get_server_address();
    QString get_port_number();
    QString get_username();

private:
    Ui::dia_server *ui;

//    QLabel      *server_label, *port_label;
//    QLineEdit   *server_lineEdit, *port_lineEdit;
};

#endif // DIA_SERVER_H
