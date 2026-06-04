import "dotenv/config";

import { PrismaMariaDb } from "@prisma/adapter-mariadb";

import { PrismaClient } from "./prisma/client.ts";

const databaseUrl = process.env.DATABASE_URL;

if (!databaseUrl) {
    throw new Error("DATABASE_URL is not set");
}

export const prisma = new PrismaClient({
    adapter: new PrismaMariaDb(databaseUrl),
});