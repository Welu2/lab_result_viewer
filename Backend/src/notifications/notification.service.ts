// src/notifications/notification.service.ts
import {
  Injectable,
  NotFoundException,
  ForbiddenException,
} from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Notification } from './notification.entity';
import { User } from 'src/users/user.entity';
import { CreateNotificationDto } from './create-notification.dto';

@Injectable()
export class NotificationService {
  constructor(
    @InjectRepository(Notification)
    private notificationRepo: Repository<Notification>,
  ) {}
  async createNotification(createNotificationDto: CreateNotificationDto) {
    // Use the correct structure for the `findOne` method
    const user = await this.notificationRepo.manager.findOne(User, {
      where: { id: createNotificationDto.userId }, // Use `where` to specify the condition
    });

    if (!user) throw new NotFoundException('User not found');

    const notification = this.notificationRepo.create({
      message: createNotificationDto.message,
      recipientType: 'user',
      user,
      type: createNotificationDto.type,
    });

    await this.notificationRepo.save(notification);
    return notification;
  }

  async notifyAdmin(message: string, type: string) {
    const notification = this.notificationRepo.create({
      message,
      recipientType: 'admin',
      type,
    });
    await this.notificationRepo.save(notification);
  }

  async notifyUser(user: User, message: string, type: string) {
    const notification = this.notificationRepo.create({
      message,
      recipientType: 'user',
      user,
      type,
    });
    await this.notificationRepo.save(notification);
  }

  async getUserNotifications(userId: number) {
    return this.notificationRepo.find({
      where: { recipientType: 'user', user: { id: userId } },
      order: { createdAt: 'DESC' },
    });
  }

  async getAdminNotifications() {
    return this.notificationRepo.find({
      where: { recipientType: 'admin' },
      order: { createdAt: 'DESC' },
    });
  }

  async markAsRead(id: number, user: any): Promise<any> {
    const notification = await this.notificationRepo.findOne({
      where: { id },
      relations: ['user'],
    });

    if (!notification) {
      throw new NotFoundException('Notification not found');
    }

    // Check if the user is owner or admin
    if (notification.user.id !== user.id && user.role !== 'admin') {
      throw new ForbiddenException(
        'You can only mark read your own notifications',
      );
    }
    await this.notificationRepo.update(id, { isRead: true });
  }
  async deleteNotification(id: number, user: any): Promise<any> {
    const notification = await this.notificationRepo.findOne({
      where: { id },
      relations: ['user'],
    });

    if (!notification) {
      throw new NotFoundException('Notification not found');
    }

    // Check if the user is owner or admin
    if (notification.user.id !== user.id && user.role !== 'admin') {
      throw new ForbiddenException(
        'You can only delete your own notifications',
      );
    }

    await this.notificationRepo.remove(notification);
    return { message: 'Notification deleted successfully' };
  }
}
