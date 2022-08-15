package org.bouncycastle.pqc.crypto.crystals.dilithium;

class PolyVecMatrix
{
    private DilithiumEngine engine;
    private int dilithiumK;
    private int dilithiumL;

    PolyVecL[] mat;

    /**
     * PolyVecL Matrix of size K
     *
     * @param engine
     */
    public PolyVecMatrix(DilithiumEngine engine)
    {
        this.engine = engine;
        this.dilithiumK = engine.getDilithiumK();
        this.dilithiumL = engine.getDilithiumL();
        this.mat = new PolyVecL[dilithiumK];

        for (int i = 0; i < dilithiumK; i++)
        {
            mat[i] = new PolyVecL(engine);
        }
    }

    public void pointwiseMontgomery(PolyVecK t, PolyVecL v)
    {
        int i;
        for (i = 0; i < dilithiumK; ++i)
        {
            t.getVectorIndex(i).pointwiseAccountMontgomery(mat[i], v);
        }
    }

    public void expandMatrix(byte[] rho)
    {
        int i, j;
        for (i = 0; i < dilithiumK; ++i)
        {
            for (j = 0; j < dilithiumL; ++j)
            {
                this.mat[i].getVectorIndex(j).uniformBlocks(rho, (short)((i << 8) + j));
            }
        }
    }

    private String addString()
    {
        String out = "[";
        int i;
        for (i = 0; i < dilithiumK; i++)
        {
            out += String.format("Outer Matrix %d [", i);
            out += this.mat[i].toString();
            if (i == dilithiumK - 1)
            {
                out += "]\n";
                continue;
            }
            out += "],\n";
        }
        out += "]\n";
        return out;
    }

    public String toString(String name)
    {
        return name.concat(": \n" + this.addString());
    }
}