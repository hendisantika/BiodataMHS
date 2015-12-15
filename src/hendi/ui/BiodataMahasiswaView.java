/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hendi.ui;

import hendi.koneksi.DatabaseConnection;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Uchiha Itachi
 */
public class BiodataMahasiswaView extends javax.swing.JFrame {

    private MahasiswaTabelModel orderMhs = new MahasiswaTabelModel();
    /**
     * Creates new form BiodataMahasiswaView
     */
    public BiodataMahasiswaView() {
        initComponents();
        tblMhs.setModel(orderMhs);
        tableMahasiswaAction();
        refresh();
        setLebarKolom();
    }
    
    private void load() {
        try {
            String sqlSelect = "SELECT * FROM tbl_mhs";
            PreparedStatement preparedStatement = DatabaseConnection.getKoneksi().prepareStatement(sqlSelect);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Mahasiswa> listMahasiswaFromDatabase = new ArrayList<>();
            while (resultSet.next()) {
                Mahasiswa biodataMahasiswa = new Mahasiswa();
                biodataMahasiswa.setNim(resultSet.getString("nim"));
                biodataMahasiswa.setNama(resultSet.getString("nama"));
                biodataMahasiswa.setJk(resultSet.getString("jenis_kelamin"));
                listMahasiswaFromDatabase.add(biodataMahasiswa);
            }
            orderMhs.setListMhs(listMahasiswaFromDatabase);
        } catch (SQLException | HeadlessException exception) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan : \nDengan Pesan : " 
                        + exception.getMessage());
        }
    }
    
    private void insert() {
        try {
            Mahasiswa mahasiswaBaru = new Mahasiswa();

            mahasiswaBaru.setNim(txtNim.getText());
            mahasiswaBaru.setNama(txtNama.getText());
            mahasiswaBaru.setJk(cbJK.getSelectedItem().toString());

            String sqlInsert = "INSERT INTO tbl_mhs VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = DatabaseConnection.getKoneksi().prepareStatement(sqlInsert);
            preparedStatement.setString(1, mahasiswaBaru.getNim());
            preparedStatement.setString(2, mahasiswaBaru.getNama());
            preparedStatement.setString(3, mahasiswaBaru.getJk());

            int isSuccess = preparedStatement.executeUpdate();
            if (isSuccess != 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
                refresh();
            } else if (isSuccess == 0) {
                JOptionPane.showMessageDialog(this, "Data gagal disimpan");
            }
        } catch (SQLException | HeadlessException exception) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan : " + exception);
        }

    }
    

    private void update(){
        try {
            int row = tblMhs.getSelectedRow();

            if (row != -1) {
                Mahasiswa mahasiswaUpdate = new Mahasiswa();

                mahasiswaUpdate.setNim(txtNim.getText());
                mahasiswaUpdate.setNama(txtNama.getText());
                mahasiswaUpdate.setJk(cbJK.getSelectedItem().toString());

                String sqlUpdate = "UPDATE tbl_mhs SET nama = ?, jenis_kelamin = ? WHERE nim = ?";
                PreparedStatement preparedStatement = DatabaseConnection.getKoneksi().prepareStatement(sqlUpdate);
                preparedStatement.setString(1, mahasiswaUpdate.getNama());
                preparedStatement.setString(2, mahasiswaUpdate.getJk());
                preparedStatement.setString(3, mahasiswaUpdate.getNim());

                int isSuccess = preparedStatement.executeUpdate();
                if (isSuccess != 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil diubah");
                    refresh();
                } else if (isSuccess == 0) {
                    JOptionPane.showMessageDialog(this, "Data gagal diubah");
                }
            }
        } catch (SQLException | HeadlessException exception) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan : " + exception);
        }
    }
    
    private void delete() {
        try {
            int row = tblMhs.getSelectedRow();

            if (row != -1) {
                Mahasiswa biodataMahasiswa = orderMhs.getMhs(row);

                String sqlDelete = "DELETE FROM tbl_mhs WHERE nim = ?";
                PreparedStatement preparedStatement = DatabaseConnection.getKoneksi().prepareStatement(sqlDelete);
                preparedStatement.setString(1, biodataMahasiswa.getNim());

                int isSuccess = preparedStatement.executeUpdate();
                if (isSuccess != 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
                    refresh();
                } else if (isSuccess == 0) {
                    JOptionPane.showMessageDialog(this, "Data gagal dihapus");
                }
            }
        } catch (SQLException | HeadlessException exception) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan : " + exception);
        }
    }
    
    private void search() {
        try {
            String sqlParameter = "";
            if (cbCari.getSelectedIndex() == 0) {
                sqlParameter = "nim";
            } else if (cbCari.getSelectedIndex() == 1) {
                sqlParameter = "nama";
            }

            String sqlSelect = "SELECT * FROM tbl_mhs WHERE " + sqlParameter + " LIKE '%" + txtCari.getText() + "%'";
            PreparedStatement preparedStatement = DatabaseConnection.getKoneksi().prepareStatement(sqlSelect);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Mahasiswa> listMahasiswaFromDatabase = new ArrayList<>();
            while (resultSet.next()) {
                Mahasiswa biodataMahasiswa = new Mahasiswa();
                biodataMahasiswa.setNim(resultSet.getString("nim"));
                biodataMahasiswa.setNama(resultSet.getString("nama"));
                biodataMahasiswa.setJk(resultSet.getString("jenis_kelamin"));
                listMahasiswaFromDatabase.add(biodataMahasiswa);
            }
            orderMhs.setListMhs(listMahasiswaFromDatabase);
        } catch (SQLException | HeadlessException exception) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan : " + exception);
        }
    }
 
    private void refresh() {
        try {
            txtNim.setEnabled(true);
            txtNim.setText(null);
            txtNama.setText(null);
            cbJK.setSelectedIndex(0);
            btnInsert.setEnabled(true);
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
            btnRefresh.setEnabled(true);
            tblMhs.clearSelection();
            cbCari.setSelectedIndex(0);
            txtCari.setText(null);
            cbCari.setEnabled(true);
            txtCari.setEnabled(true);
            load();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan : " + exception);
        }
    }
   
    private void tableMahasiswaAction() {
        try {
            tblMhs.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    int row = tblMhs.getSelectedRow();

                    if (row != -1) {
                        Mahasiswa biodataMahasiswa = orderMhs.getMhs(row);

                        txtNim.setText(biodataMahasiswa.getNim());
                        txtNama.setText(biodataMahasiswa.getNama());
                        cbJK.setSelectedItem(biodataMahasiswa.getJk());

                        txtNim.setEnabled(false);
                        btnInsert.setEnabled(false);
                        btnUpdate.setEnabled(true);
                        btnDelete.setEnabled(true);
                        cbCari.setEnabled(false);
                        txtCari.setEnabled(false);
                    }
                }
            });
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan : " + exception);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNim = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        cbJK = new javax.swing.JComboBox();
        btnInsert = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMhs = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        cbCari = new javax.swing.JComboBox();
        txtCari = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabel1.setText("NIM");

        jLabel2.setText("NAMA");

        jLabel3.setText("JENIS KELAMIN");

        cbJK.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Laki-laki", "Perempuan" }));

        btnInsert.setText("Insert");
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        tblMhs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblMhs);

        jLabel4.setText("Search :");

        cbCari.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NIM", "NAMA", "JENIS KELAMIN" }));

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbJK, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNim, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnInsert)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnKeluar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbCari, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbJK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsert)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnRefresh)
                    .addComponent(btnKeluar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        // TODO add your handling code here:
        search();
    }//GEN-LAST:event_txtCariKeyReleased

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        Dimension posisi = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (posisi.width - this.getWidth()) / 2;
        int y = (posisi.height - this.getHeight()) /2;
        this.setLocation(x, y);
    }//GEN-LAST:event_formWindowActivated

    public void setColumnWidth(int kolom){
		DefaultTableColumnModel modelKolom = (DefaultTableColumnModel) tblMhs.getColumnModel();
		TableColumn kolomTabel = modelKolom.getColumn(kolom);
		int lebar = 0;
		int margin = 10;
		int a;
		
		TableCellRenderer renderer = kolomTabel.getHeaderRenderer();
		if(renderer == null)
		{
			renderer = tblMhs.getTableHeader().getDefaultRenderer();
		}
		Component komponen = renderer.getTableCellRendererComponent(tblMhs, kolomTabel.getHeaderValue(), false, false, 0, 0);
		lebar = komponen.getPreferredSize().width;
			for(a=0; a < tblMhs.getRowCount();a++)
			{
				renderer = tblMhs.getCellRenderer(a, kolom);
				komponen = renderer.getTableCellRendererComponent(tblMhs, tblMhs.getValueAt(a, kolom), false, false, a, kolom);
				int lebarKolom = komponen.getPreferredSize().width;
				lebar = Math.max(lebar, lebarKolom);
				
			}
			lebar  = lebar + margin;
			kolomTabel.setPreferredWidth(lebar);
	}
	
	public void setLebarKolom()
	{
		int a;
		for(a=0; a < tblMhs.getColumnCount(); a++)
		{
			setColumnWidth(a);
		}
	}
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BiodataMahasiswaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BiodataMahasiswaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BiodataMahasiswaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BiodataMahasiswaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BiodataMahasiswaView().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox cbCari;
    private javax.swing.JComboBox cbJK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblMhs;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNim;
    // End of variables declaration//GEN-END:variables
}
