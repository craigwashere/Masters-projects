#include "dia_server.h"
#include "ui_dia_server.h"

dia_server::dia_server(QWidget *parent) :   QDialog(parent), ui(new Ui::dia_server)
{
    ui->setupUi(this);
    ui->server_lineEdit->setFocus();
}

dia_server::~dia_server()
{
    delete ui;
}

QString dia_server::get_server_address()    {   return ui->server_lineEdit->text();     }
QString dia_server::get_port_number()       {   return ui->port_lineEdit->text();       }
QString dia_server::get_username()          {   return ui->username_lineEdit->text();   }

// need to override the done(r) method to validate texts
