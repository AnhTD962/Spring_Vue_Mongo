import { defineStore } from "pinia";
import { getProfile, updateProfile, changePassword } from "../api/auth";

export const useUserStore = defineStore("user", {
    state: () => ({
        profile: null,
    }),
    actions: {
        async fetchProfile() {
            const { data } = await getProfile();
            this.profile = data;
        },
        async updateProfile(profileData) {
            const { data } = await updateProfile(profileData);
            this.profile = data;
        },
        async changePassword(payload) {
            await changePassword(payload);
        }
    }
});