
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class SparseVector implements Serializable {


    public SparseVector(float[] vector) {
        this(vector, -1);
    }


    public SparseVector(float[] vector, int id) {
        assert (vector != null) : "ERROR: vector can not be null!";

        this.id = id;

        this.size = vector.length;

        ArrayList<Integer> index_aux = new ArrayList<Integer>();
        ArrayList<Float> values_aux = new ArrayList<Float>();

        for (int i = 0; i < vector.length; i++) {
            if (vector[i] > 0.0f) {
                index_aux.add(i);
                values_aux.add(vector[i]);
            }
        }

        this.index = new int[index_aux.size()];
        this.values = new float[values_aux.size()];

        int length = this.index.length;
        for (int i = 0; i < length; i++) {
            this.index[i] = index_aux.get(i);
            this.values[i] = values_aux.get(i);
            this.norm += this.values[i] * this.values[i];
        }

        this.norm = (float) Math.sqrt(this.norm);
    }

    public float dot(SparseVector svector) {
        assert (this.size == svector.size) : "ERROR: vectors of different sizes!";

        float innerprod = 0.0f;

        int length = this.index.length;
        int vlength = svector.index.length;
        int[] vindex = svector.index;
        float[] vvalues = svector.values;

        if (length > 0 && index[0] <= vindex[vlength - 1]) {
            for (int i = 0,  j = 0; i < length; i++) {
                while (j + 1 <= vlength && vindex[j] < this.index[i]) {
                    j++;
                }

                if (j >= vlength) {
                    break;
                } else if (this.index[i] == vindex[j]) {
                    innerprod += this.values[i] * vvalues[j];
                    j++;
                }
            }
        }

        return innerprod;
    }

    public float norm() {
        return this.norm;
    }

    public void normalize() {
        assert (this.norm != 0.0f) : "ERROR: it is not possible to normalize a null vector!";

        if (this.norm > DELTA) {
            int length = this.values.length;
            float aux = 0.0f;
            for (int i = 0; i < length; i++) {
                values[i] = values[i] / this.norm;
                aux += values[i] * values[i];
            }
            this.norm = aux;
        } else {
            this.norm = 0.0f;
        }
    }

    public int size() {
        return this.size;
    }

    public float sparsity() {
        if (this.size > 0) {
            return 1.0f - ((float) this.index.length / (float) this.size);
        } else {
            return 1.0f;
        }
    }

    public int[] getIndex() {
        return index;
    }

    public float[] getValues() {
        return values;
    }

    public float getValue(int index) {
        for (int i = 0; i < this.index.length; i++) {
            if (this.index[i] == index) {
                return this.values[i];
            } else if (this.index[i] > index) {
                return 0.0f;
            }
        }
        return 0.0f;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public float[] toDenseVector() {
        float[] vector = new float[this.size];
        Arrays.fill(vector, 0.0f);

        int length = this.index.length;
        for (int i = 0; i < length; i++) {
            vector[this.index[i]] = this.values[i];
        }

        return vector;
    }

    @Override
    public Object clone() {
        SparseVector clone = new SparseVector(new float[]{0});
        clone.norm = this.norm;
        clone.size = this.size;
        clone.id = this.id;

        clone.index = new int[this.index.length];
        System.arraycopy(this.index, 0, clone.index, 0, this.index.length);

        clone.values = new float[this.values.length];
        System.arraycopy(this.values, 0, clone.values, 0, this.values.length);

        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        SparseVector spv = ((SparseVector) obj);
        int length = this.index.length;
        int spvlength = spv.index.length;

        for (int i = 0; i < length; i++) {
            if (spvlength > i) {
                if (this.index[i] != spv.index[i] ||
                        Math.abs(this.values[i] - spv.values[i]) > DELTA) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return (Math.abs(spv.norm - this.norm) <= DELTA && spv.size == this.size);
    }

    private static final float DELTA = 0.00001f;
    private float norm = 0.0f;
    private int size = 0;
    private int[] index;
    private float[] values;
    private int id;
    private static final long serialVersionUID = 27L;
}
