package domain;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 1L;
   protected int id;

   public Entity (int id){
       this.id = id;
   }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Entity))
            return false;

        Entity other = (Entity) obj;
        return this.getId() == other.getId();
    }

    public int getId (){
       return this.id;

   }
}
