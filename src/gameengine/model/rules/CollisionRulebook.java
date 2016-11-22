package gameengine.model.rules;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import gameengine.controller.RuleActionHandler;
import objects.GameObject;
import utils.ReflectionUtil;
import utils.ResourceReader;

public class CollisionRulebook{
	private ResourceReader resources;
	private RuleActionHandler handler;
	
	public CollisionRulebook(RuleActionHandler handler) {
		resources = new ResourceReader("GameEngineCollisionProperties");
		this.handler = handler;
	}
	//might need to fully specify classpath to rule in properties file, instead of just rule name
	 
	public void applyRules(GameObject mainChar, GameObject obj, RuleActionHandler handler) throws ClassNotFoundException{
		for(String property: obj.getPropertiesList()){
			String ruleName = resources.getResource(property);
			ruleName = "gameengine.model.rules.collisionrules." + ruleName;
			try {
				System.out.println(this.getClass().getName());
				System.out.println(ruleName);
				Method method = ReflectionUtil.getMethodFromClass(ruleName, "applyRule",  new Class[]{RuleActionHandler.class, GameObject.class, GameObject.class});
				
				method.invoke(null, handler, mainChar, obj);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | ClassNotFoundException | NoSuchMethodException
					| SecurityException e) {
				e.printStackTrace();
				//throw new ClassNotFoundException();
			}
		}
	}
}
