



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class SparseMatrix implements Serializable {

    private static final long serialVersionUID = 27L;
    /** Creates a new instance of SparseMatrix */
    public SparseMatrix() {
    }

    public SparseMatrix(String filename) throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(new File(filename));
            ois = new ObjectInputStream(fis);
            SparseMatrix aux = (SparseMatrix) ois.readObject();
            this.rows = aux.rows;
        } catch (IOException e) {
            throw new IOException("Problems reading \"" + filename + "\"!");
        } catch (ClassNotFoundException ex) {
            throw new IOException("Problems reading \"" + filename + "\"!");
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void save(String filename) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(new File(filename));
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
        } catch (IOException e) {
            throw new IOException("Problems writing \"" + filename + "\"!");
        } finally {
            if (oos != null) {
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public boolean contains(SparseVector vector) {
        return this.rows.contains(vector);
    }

    public void addRow(SparseVector vector) {
        assert (rows.isEmpty() || this.rows.get(0).size() == vector.size()) :
                "ERROR: vector of wrong size!";
        this.rows.add(vector);
    }

    public void addRow(float[] vector, int id) {
        SparseVector sv = new SparseVector(vector, id);
        this.addRow(sv);
    }

    public int getRowsCount() {
        return this.rows.size();
    }

    public float sparsity() {
        float sparsity = 0.0f;

        int size = this.rows.size();
        for (int i = 0; i < size; i++) {
            sparsity += this.rows.get(i).sparsity();
        }

        if (this.rows.size() > 0) {
            return sparsity / this.rows.size();
        } else {
            return 0.0f;
        }
    }

    public SparseVector getRow(int row) {
        assert (rows.size() > row) : "ERROR: this row does not exists in the matrix!";
        return this.rows.get(row);
    }

    public void normalize() {
        int size = this.rows.size();
        for (int i = 0; i < size; i++) {
            this.rows.get(i).normalize();
        }
    }

    public float[][] toDenseMatrix() {
        float[][] matrix = new float[this.rows.size()][];

        int size = this.rows.size();
        for (int i = 0; i < size; i++) {
            matrix[i] = this.rows.get(i).toDenseVector();
        }
        return matrix;
    }

    public ArrayList<String> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(ArrayList<String> attributes) {
        assert (rows.isEmpty() || this.rows.get(0).size() == attributes.size()) :
                "ERROR: attributes and vectors of different sizes!";

        this.attributes = attributes;
    }

    private ArrayList<String> attributes = new ArrayList<String>();
    private ArrayList<SparseVector> rows = new ArrayList<SparseVector>();
}
