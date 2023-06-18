import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	plugins: [sveltekit()],
	define: {
		'process.env.URLAPI': JSON.stringify('http://172.24.1.20:8080')
	},
	server: {
		port: 3000
	}
});
