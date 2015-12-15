/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hendi.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Uchiha Itachi
 */
public class MahasiswaTabelModel extends AbstractTableModel{

    private List<Mahasiswa> listMahasiswa = new ArrayList<>();

    public Mahasiswa getMhs(int index){
        return listMahasiswa.get(index);
    }    
    
    public void setListMhs(List<Mahasiswa> listMhsBaru){
        listMahasiswa = listMhsBaru;
        fireTableDataChanged();
    }
    
    
    @Override
    public int getRowCount() {
        return listMahasiswa.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return rowIndex + 1;
            case 1:
                return listMahasiswa.get(rowIndex).getNim();
            case 2:
                return listMahasiswa.get(rowIndex).getNama();
            case 3:
                return listMahasiswa.get(rowIndex).getJk();
            default:
                return null;
                
        }
    }
    
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0:
                return "No";
            case 1:
                return "NIM";
            case 2:
                return "Nama Lengkap";
            case 3:
                return "Jenis Kelamin";
            default:
                return null;
        }
    }
    
    
}
