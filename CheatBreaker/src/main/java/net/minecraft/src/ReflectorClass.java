package net.minecraft.src;

public class ReflectorClass
{
    private String[] targetClassNames = null;
    private boolean checked = false;
    private Class targetClass = null;

    public ReflectorClass(String targetClassName)
    {
        this.targetClassNames = new String[] {targetClassName};
        Class cls = this.getTargetClass();
    }

    public ReflectorClass(String[] targetClassNames)
    {
        this.targetClassNames = targetClassNames;
        Class cls = this.getTargetClass();
    }

    public ReflectorClass(Class targetClass)
    {
        this.targetClass = targetClass;
        this.targetClassNames = new String[] {targetClass.getName()};
        this.checked = true;
    }

    public Class getTargetClass()
    {
        if (this.checked)
        {
            return this.targetClass;
        }
        else
        {
            this.checked = true;

            for (int i = 0; i < this.targetClassNames.length; ++i)
            {
                String targetClassName = this.targetClassNames[i];

                try
                {
                    this.targetClass = Class.forName(targetClassName);
                    break;
                }
                catch (ClassNotFoundException var4)
                {
                    Config.log("(Reflector) Class not present: " + targetClassName);
                }
                catch (Throwable var5)
                {
                    var5.printStackTrace();
                }
            }

            return this.targetClass;
        }
    }

    public boolean exists()
    {
        return this.getTargetClass() != null;
    }
}
