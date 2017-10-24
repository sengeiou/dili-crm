package com.dili.alm.constant;

/**
 * Created by asiamaster on 2017/10/24 0024.
 */
public class AlmConstants {
	//团队成员状态: 加入/离开
	public static enum MemberState {
		LEAVE(0),
		JOIN(1);

		private int code;

		MemberState(int code) {
			this.code = code;
		}

		/**
		 * 根据类型编码获取MemberState
		 *
		 * @param code
		 * @return
		 */
		public static MemberState getMemberStateByCode(int code) {
			for (MemberState memberState : MemberState.values()) {
				if (memberState.getCode() == code) {
					return memberState;
				}
			}
			return null;
		}

		public int getCode() {
			return code;
		}
	}
}
